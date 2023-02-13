package com.xdteam.fotofiesta.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.xdteam.fotofiesta.data.repository.SettingsRepositoryImpl
import com.xdteam.fotofiesta.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.SettingsModule: DataStore<Preferences> by preferencesDataStore(
    name = "com.xdteam.fotofiesta.settings"
)

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Provides
        @Singleton
        fun provideSettingsPreferences(@ApplicationContext context: Context): DataStore<Preferences> =
            context.SettingsModule
    }
}