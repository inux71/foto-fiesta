package com.xdteam.fotofiesta.api

import com.xdteam.fotofiesta.api.body.UploadFilesBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IApi {
    @GET("pdf/{filename}")
    suspend fun downloadFile(@Path("filename") filename: String): Response<ResponseBody>

    @POST("/files")
    fun uploadFiles(@Body body: UploadFilesBody): Call<String>
}