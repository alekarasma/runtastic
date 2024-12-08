package com.asmaa.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.asmaa.core.database.dao.RunPendingSyncDao
import com.asmaa.core.domain.run.RemoteDataSource
import com.asmaa.core.domain.run.RunRepository

class DeleteRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteDataSource: RemoteDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val runId = params.inputData.getString("RUN_ID") ?: return Result.failure()
        return when (val result = remoteDataSource.deleteRun(runId)) {
            is com.asmaa.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.asmaa.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteDeletedRunSyncEntity(runId)
                Result.success()
            }
        }

    }
}