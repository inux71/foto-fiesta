package com.xdteam.fotofiesta.domain.repository

import com.xdteam.fotofiesta.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getAll(): Flow<List<Image>>
    suspend fun getById(id: Long): Image
    suspend fun insert(image: Image)
}