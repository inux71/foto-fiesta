package com.xdteam.fotofiesta.api

import retrofit2.Retrofit

object ApiUtilities {
    val BASE_URL = "https://localhost:8080"

    fun getInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()
}