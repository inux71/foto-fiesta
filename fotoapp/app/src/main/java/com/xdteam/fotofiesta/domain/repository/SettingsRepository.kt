package com.xdteam.fotofiesta.domain.repository

interface SettingsRepository {
    suspend fun getDelay(): Result<Int>
    suspend fun setDelay(delay: Int)

    suspend fun getNumberOfPhotos(): Result<Int>
    suspend fun setNumberOfPhotos(numberOfPhotos: Int)
}
