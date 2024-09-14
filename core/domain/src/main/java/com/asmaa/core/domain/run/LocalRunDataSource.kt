package com.asmaa.core.domain.run


import com.asmaa.core.domain.util.DataError.*
import kotlinx.coroutines.flow.Flow
import com.asmaa.core.domain.util.Result

typealias RunId = String

interface LocalRunDataSource {

    suspend fun upsertRun(run: Run): Result<RunId, Local>

    suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, Local>

    fun getRuns(): Flow<List<Run>>

    suspend fun deleteRun(id: String)

    suspend fun deleteAllRun()
}
