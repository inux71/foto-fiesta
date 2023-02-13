package com.xdteam.fotofiesta.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.xdteam.fotofiesta.domain.model.Settings
import com.xdteam.fotofiesta.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val userSettingPreferences: DataStore<Preferences>
) : SettingsRepository {
    override suspend fun getDelay(): Result<Int> {
        return Result.runCatching {
            val flow = userSettingPreferences.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[KEY_DELAY]
                }

            val value = flow.firstOrNull() ?: 3

            value
        }
    }

    override suspend fun setDelay(delay: Int) {
        Result.runCatching {
            userSettingPreferences.edit { preferences ->
                preferences[KEY_DELAY] = delay
            }
        }
    }

    override suspend fun getNumberOfPhotos(): Result<Int> {
        return Result.runCatching {
            val flow = userSettingPreferences.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[KEY_NUMBER_OF_PHOTOS]
                }

            val value = flow.firstOrNull() ?: 3

            value
        }
    }

    override suspend fun setNumberOfPhotos(numberOfPhotos: Int) {
        Result.runCatching {
            userSettingPreferences.edit { preferences ->
                preferences[KEY_NUMBER_OF_PHOTOS] = numberOfPhotos
            }
        }
    }

    companion object {
        val KEY_DELAY = intPreferencesKey("delay")
        val KEY_NUMBER_OF_PHOTOS = intPreferencesKey("number_of_photos")
    }
}