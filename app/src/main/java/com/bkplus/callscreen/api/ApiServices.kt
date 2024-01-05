package com.harison.core.app.api

import com.harison.core.app.api.entity.ResponseApi
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>

    @GET("home_section.json")
    suspend fun getApiData(): BaseResponse<ResponseApi>
}