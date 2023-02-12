package com.xdteam.fotofiesta.data.repository

import com.xdteam.fotofiesta.api.IApi
import com.xdteam.fotofiesta.api.body.UploadFilesBody
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class PDFRepositoryImpl(private val _api: IApi) : PDFRepository {
    override suspend fun downloadFile(filename: String): Response<ResponseBody> =
        _api.downloadFile(filename)

    override fun uploadFiles(files: List<File>, serieId: String): Call<String> =
        _api.uploadFiles(UploadFilesBody(files, serieId))
}