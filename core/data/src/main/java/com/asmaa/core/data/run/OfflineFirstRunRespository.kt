package com.asmaa.core.data.run

import com.asmaa.core.database.dao.RunPendingSyncDao
import com.asmaa.core.database.mappers.toRun
import com.asmaa.core.domain.SessionStorage
import com.asmaa.core.domain.run.LocalRunDataSource
import com.asmaa.core.domain.run.RemoteDataSource
import com.asmaa.core.domain.run.Run
import com.asmaa.core.domain.run.RunId
import com.asmaa.core.domain.run.RunRepository
import com.asmaa.core.domain.util.DataError
import com.asmaa.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import com.asmaa.core.domain.util.Result
import com.asmaa.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localRunDataSource: LocalRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success ->
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        val runWithId = run.copy(id = localResult.data)
        val remoteResult = remoteDataSource.postRun(runWithId, mapPicture)
        return when (remoteResult) {
            //TODO - what is this actually returning?? //Understnad as Empty Data result
            is Result.Error -> remoteResult.asEmptyDataResult()
            is Result.Success -> applicationScope.async {
                localRunDataSource.upsertRun(run).asEmptyDataResult()
            }.await()
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null

        if(isPendingSync)
        {
            runPendingSyncDao.deleteDeletedRunSyncEntity(id)
        }
        applicationScope.async {
            remoteDataSource.deleteRun(id)
        }.await()
    }

    override suspend fun runPendingSync() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userID ?: return@withContext
            val createdRuns = async { runPendingSyncDao.getAllRunPendingSyncEntity(userId) }
            val deletedRuns = async { runPendingSyncDao.getAllDeletedRunSyncEntities(userId) }

            val createdJob = createdRuns
                .await()
                .map {
                    launch {
                        when (remoteDataSource.postRun(it.run.toRun(), it.mapPictureByteArray)) {
                            is Result.Error -> Unit
                            is Result.Success -> applicationScope.launch {
                                runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                            }.join()
                        }
                    }
                }

            val deletedJob = deletedRuns
                .await()
                .map {
                    launch {
                        when (remoteDataSource.deleteRun(it.id)) {
                            is Result.Error -> Unit
                            is Result.Success -> applicationScope.launch {
                                runPendingSyncDao.deleteDeletedRunSyncEntity(it.id)
                            }.join()
                        }
                    }
                }

            createdJob.forEach { it.join() }
            deletedJob.forEach { it.join() }
        }
    }
}