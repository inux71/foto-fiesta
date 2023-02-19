package com.xdteam.fotofiesta.domain.repository

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

interface PDFRepository {
    suspend fun downloadFile(filename: String): String?
    suspend fun uploadFiles(files: List<File>, serieId: String): Response<ResponseBody>
}
