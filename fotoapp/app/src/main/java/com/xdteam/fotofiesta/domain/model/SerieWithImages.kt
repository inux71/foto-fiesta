package com.xdteam.fotofiesta.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class SerieWithImages(
    @Embedded val serie: Serie,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val images: List<Image>
)
