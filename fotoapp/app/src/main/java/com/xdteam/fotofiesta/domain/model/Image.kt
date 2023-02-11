package com.xdteam.fotofiesta.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val serieId: Long,
    val path: String
)
