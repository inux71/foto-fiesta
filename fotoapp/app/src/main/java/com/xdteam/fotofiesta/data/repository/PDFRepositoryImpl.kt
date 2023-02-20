package com.xdteam.fotofiesta.data.repository

import android.os.Environment
import android.util.Log
import com.xdteam.fotofiesta.api.IApi
import com.xdteam.fotofiesta.domain.repository.PDFRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
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

    override suspend fun uploadFiles(files: List<File>, serieId: String): Response<ResponseBody> {
        return _api.uploadFiles(
            files.map { file ->
                MultipartBody.Part.createFormData(
                    "files",
                    file.name,
                    file.asRequestBody()
                )
            },
            MultipartBody.Part.createFormData(
                "seriesId",
                serieId
            )
        )
    }
}
