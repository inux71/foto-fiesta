package com.xdteam.fotofiesta.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface IApi {
    @GET("pdf/{filename}")
    suspend fun downloadFile(@Path("filename") filename: String): Response<ResponseBody>

    @Multipart
    @POST("/files")
    suspend fun uploadFiles(@Part files: List<MultipartBody.Part>, @Part serieId: MultipartBody.Part): Response<ResponseBody>
}
