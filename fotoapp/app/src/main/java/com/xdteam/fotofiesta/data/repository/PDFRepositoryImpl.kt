package com.xdteam.fotofiesta.data.repository

import android.os.Environment
import android.util.Log
import com.xdteam.fotofiesta.api.IApi
import com.xdteam.fotofiesta.api.body.UploadFilesBody
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import retrofit2.Call
import java.io.File

class PDFRepositoryImpl(private val _api: IApi) : PDFRepository {
    override suspend fun downloadFile(filename: String): String? {
        val response = _api.downloadFile(filename)
        Log.i("RESPONSE", response.toString())
        Log.i("SCIEZKA", Environment.getDownloadCacheDirectory().toString())

        if (response.isSuccessful) {
            val pdf = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            pdf.writeBytes(response.body()!!.bytes())

            return pdf.parent
        }

        return null
    }

    override fun uploadFiles(files: List<File>, serieId: String): Call<String> =
        _api.uploadFiles(UploadFilesBody(files, serieId))
}
