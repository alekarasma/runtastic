package com.asmaa.run.data

import androidx.work.ListenableWorker
import com.asmaa.core.domain.util.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when (this) {
        DataError.Local.DISK_FULL -> ListenableWorker.Result.failure()
        DataError.NetworkError.REQUEST_TIMEOUT -> ListenableWorker.Result.success()
        DataError.NetworkError.UNAUTHORIZED -> ListenableWorker.Result.success()
        DataError.NetworkError.FORBIDDEN -> ListenableWorker.Result.success()
        DataError.NetworkError.CONFLICT -> ListenableWorker.Result.success()
        DataError.NetworkError.NOT_FOUND -> ListenableWorker.Result.success()
        DataError.NetworkError.TOO_MANY_REQUESTS -> ListenableWorker.Result.success()
        DataError.NetworkError.NO_INTERNET -> ListenableWorker.Result.success()
        DataError.NetworkError.PAYLOAD_TOO_LARGE -> ListenableWorker.Result.failure()
        DataError.NetworkError.SERVER_ERROR -> ListenableWorker.Result.success()
        DataError.NetworkError.SERIALIZATION -> ListenableWorker.Result.failure()
        DataError.NetworkError.UNKNOWN_ERROR -> ListenableWorker.Result.failure()
    }
}