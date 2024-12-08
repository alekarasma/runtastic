package com.asmaa.core.domain.run

import com.asmaa.core.domain.util.DataError
import com.asmaa.core.domain.util.EmptyResult
import com.asmaa.core.domain.util.Result

interface RemoteDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.NetworkError>
    suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.NetworkError>
    suspend fun deleteRun(id: String): EmptyResult<DataError.NetworkError>
}