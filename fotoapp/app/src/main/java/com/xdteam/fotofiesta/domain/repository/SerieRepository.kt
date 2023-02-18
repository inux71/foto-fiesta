package com.xdteam.fotofiesta.domain.repository

import com.xdteam.fotofiesta.domain.model.Serie
import com.xdteam.fotofiesta.domain.model.SerieWithImages
import kotlinx.coroutines.flow.Flow

interface SerieRepository {
    fun getSeries(): Flow<List<SerieWithImages>>
    suspend fun getSerieById(id: Long): SerieWithImages
    suspend fun insertSerie(serie: Serie): Long
}
