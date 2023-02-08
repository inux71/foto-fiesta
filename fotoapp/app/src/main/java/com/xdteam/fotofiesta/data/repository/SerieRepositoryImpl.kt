package com.xdteam.fotofiesta.data.repository

import com.xdteam.fotofiesta.data.source.dao.SerieDao
import com.xdteam.fotofiesta.domain.model.Serie
import com.xdteam.fotofiesta.domain.model.SerieWithImages
import com.xdteam.fotofiesta.domain.repository.SerieRepository
import kotlinx.coroutines.flow.Flow

class SerieRepositoryImpl(
    private val _dao: SerieDao
) : SerieRepository {
    override fun getSeries(): Flow<List<SerieWithImages>> = _dao.getSeries()
    override suspend fun getSerieById(id: Long): SerieWithImages = _dao.getSerieById(id)
    override suspend fun insertSerie(serie: Serie) = _dao.insertSerie(serie)
}
