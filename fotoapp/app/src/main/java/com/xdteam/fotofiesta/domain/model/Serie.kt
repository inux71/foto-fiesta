package com.xdteam.fotofiesta.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serie")
data class Serie(
    @PrimaryKey(autoGenerate = true) val id: Long
)
