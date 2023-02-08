package com.xdteam.fotofiesta.data.source

import androidx.room.Database
import com.xdteam.fotofiesta.domain.model.Image
import com.xdteam.fotofiesta.domain.model.Serie

@Database(entities = [Image::class, Serie::class], version = 1)
class AppDatabase {
}