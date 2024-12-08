package com.asmaa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asmaa.core.database.dao.RunDao
import com.asmaa.core.database.dao.RunPendingSyncDao
import com.asmaa.core.database.enitity.RunDeleteSyncEntity
import com.asmaa.core.database.enitity.RunEntity
import com.asmaa.core.database.enitity.RunPendingSyncEntity

@Database(entities = [RunEntity::class, RunPendingSyncEntity::class, RunDeleteSyncEntity::class], version = 1)
abstract class RunDatabase : RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
}