package com.asmaa.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.asmaa.core.database.dao.RunPendingSyncDao
import com.asmaa.core.database.mappers.toRun
import com.asmaa.core.domain.run.RemoteDataSource

class CreateRunWorker(
    context: Context, private var params: WorkerParameters, private val remoteDataSource: RemoteDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val runId = params.inputData.getString("RUN_ID") ?: return Result.failure()
        val pendingRunEntity = pendingSyncDao.getRunPendingSyncEntity(runId) ?: return Result.failure()
        val run = pendingRunEntity.run.toRun()
        return when (val result = remoteDataSource.postRun(run, pendingRunEntity.mapPictureByteArray)) {
            is com.asmaa.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.asmaa.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteRunPendingSyncEntity(runId)
                Result.success()
            }
        }
    }
}