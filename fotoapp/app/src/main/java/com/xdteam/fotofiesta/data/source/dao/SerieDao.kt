package com.xdteam.fotofiesta.data.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xdteam.fotofiesta.domain.model.Serie
import com.xdteam.fotofiesta.domain.model.SerieWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieDao {
    @Query("SELECT * FROM serie")
    fun getSeries(): Flow<List<SerieWithImages>>

    @Query("SELECT * FROM serie WHERE id LIKE :id")
    suspend fun getSerieById(id: Long): SerieWithImages

    @Insert
    suspend fun insertSerie(serie: Serie): Long
}
