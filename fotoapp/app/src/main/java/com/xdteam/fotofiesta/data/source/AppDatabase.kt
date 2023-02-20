package com.xdteam.fotofiesta.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xdteam.fotofiesta.data.source.dao.ImageDao
import com.xdteam.fotofiesta.data.source.dao.SerieDao
import com.xdteam.fotofiesta.domain.model.Image
import com.xdteam.fotofiesta.domain.model.Serie

@Database(entities = [Image::class, Serie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val serieDao: SerieDao
    abstract val imageDao: ImageDao

    companion object {
        const val DATABASE_NAME = "app-database"
    }
}
