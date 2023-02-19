package com.xdteam.fotofiesta.data.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xdteam.fotofiesta.domain.model.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    fun getAll(): Flow<List<Image>>

    @Query("SELECT * FROM image WHERE id LIKE :id")
    suspend fun getById(id: Long): Image

    @Insert
    suspend fun insert(image: Image)
}