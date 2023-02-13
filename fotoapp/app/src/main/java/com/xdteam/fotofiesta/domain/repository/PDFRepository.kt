package com.xdteam.fotofiesta.domain.repository

import retrofit2.Call
import java.io.File

interface PDFRepository {
    suspend fun downloadFile(filename: String): String?
    fun uploadFiles(files: List<File>, serieId: String): Call<String>
}
