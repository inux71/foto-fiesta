package com.xdteam.fotofiesta.data.repository

import android.os.Environment
import android.util.Log
import com.xdteam.fotofiesta.api.IApi
import com.xdteam.fotofiesta.api.body.UploadFilesBody
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class PDFRepositoryImpl(private val _api: IApi) : PDFRepository {
    override suspend fun downloadFile(filename: String) {
        val response = _api.downloadFile(filename)
        Log.i("RESPONSE", response.toString())
        Log.i("SCIEZKA", Environment.getDownloadCacheDirectory().toString())

        if (response.isSuccessful) {
            val pdf = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            pdf.writeBytes(response.body()!!.bytes())
        }
    }

    override fun uploadFiles(files: List<File>, serieId: String): Call<String> =
        _api.uploadFiles(UploadFilesBody(files, serieId))
}