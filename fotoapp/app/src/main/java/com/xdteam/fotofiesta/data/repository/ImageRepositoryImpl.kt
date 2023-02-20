package com.xdteam.fotofiesta.data.repository

import com.xdteam.fotofiesta.data.source.dao.ImageDao
import com.xdteam.fotofiesta.domain.model.Image
import com.xdteam.fotofiesta.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl(
    private val _dao: ImageDao
) : ImageRepository {
    override fun getAll(): Flow<List<Image>> = _dao.getAll()
    override suspend fun getById(id: Long): Image = _dao.getById(id)
    override suspend fun insert(image: Image) = _dao.insert(image)
}
