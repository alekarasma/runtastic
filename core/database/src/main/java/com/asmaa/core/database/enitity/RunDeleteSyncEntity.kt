package com.asmaa.core.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asmaa.core.domain.run.RunId

@Entity
data class RunDeleteSyncEntity(

    @PrimaryKey(autoGenerate = false)
    val id: RunId,
    val userId: String
)