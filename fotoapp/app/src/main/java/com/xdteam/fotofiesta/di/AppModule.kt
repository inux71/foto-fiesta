package com.xdteam.fotofiesta.di

import android.app.Application
import androidx.room.Room
import com.xdteam.fotofiesta.api.ApiUtilities
import com.xdteam.fotofiesta.api.IApi
import com.xdteam.fotofiesta.data.repository.PDFRepositoryImpl
import com.xdteam.fotofiesta.data.repository.SerieRepositoryImpl
import com.xdteam.fotofiesta.data.source.AppDatabase
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import com.xdteam.fotofiesta.domain.repository.SerieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideSerieRepository(database: AppDatabase): SerieRepository =
        SerieRepositoryImpl(database.serieDao)

    @Provides
    @Singleton
    fun providePDFRepository(api: IApi): PDFRepository = PDFRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAPIUtilities(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiUtilities.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit): IApi = retrofit.create(IApi::class.java)
}
