package com.bkplus.callscreen.api

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

@Singleton
class FileDownloader @Inject constructor(
    @Named("App context") private val context: Context,
    private val apiService: ApiService
) {
    suspend fun downloadFile(
        fileUrl: String,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val response: Response<ResponseBody> = apiService.downloadFile(fileUrl)
            onStart()
            if (response.isSuccessful) {
                val responseBody = response.body()
                val fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1)
                Log.e("----", "file name: $fileName")
                if (responseBody != null) {
                    saveFile(responseBody.byteStream(), fileName)
                    onSuccess()
                } else {
                    onError("Response body is empty.")
                }
            } else {
                onError("Download failed with code ${response.code()}")
            }
        } catch (e: Exception) {
            onError("Download failed: ${e.message}")
        }
    }

    private suspend fun saveFile(inputStream: InputStream, fileName: String) {
        withContext(Dispatchers.IO) {
            val outputFolder = File(context.filesDir, "downloads")
            Log.e("----", "output folder: ${outputFolder.absolutePath}")
            if (!outputFolder.exists()) {
                outputFolder.mkdirs()
            }
            val outputFile = File(outputFolder, fileName)
            try {
                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileExtension(contentType: String?): String {
        return when (contentType) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            else -> "png"
        }
    }
}
