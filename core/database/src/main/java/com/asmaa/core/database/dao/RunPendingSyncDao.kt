package com.asmaa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.asmaa.core.database.enitity.RunDeleteSyncEntity
import com.asmaa.core.database.enitity.RunPendingSyncEntity
import com.asmaa.core.domain.run.RunId

@Dao
interface RunPendingSyncDao {

    @Query("SELECT * FROM RunPendingSyncEntity WHERE userId=:userId")
    suspend fun getAllRunPendingSyncEntity(userId:String):List<RunPendingSyncEntity>

    @Query("SELECT * FROM RunPendingSyncEntity WHERE runId=:runId")
    suspend fun getRunPendingSyncEntity(runId: RunId):RunPendingSyncEntity
    @Upsert
    suspend fun upsertRunPendingSyncEntity(entity: RunPendingSyncEntity)

    @Query("DELETE FROM RunPendingSyncEntity WHERE runId=:runId")
    suspend fun deleteRunPendingSyncEntity(runId: RunId)

    @Query("DELETE FROM RunDeleteSyncEntity WHERE id=:runId")
    suspend fun deleteDeletedRunSyncEntity(runId: RunId)

    @Query("SELECT * FROM RunDeleteSyncEntity WHERE id=:runId")
    suspend fun getAllDeletedRunSyncEntities(runId: RunId) : List<RunDeleteSyncEntity>
}