package com.bkplus.callscreen.api

import com.bkplus.callscreen.api.entity.HomeSectionEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>

    @GET("home-sections.json")
    suspend fun getApiData(): BaseResponse<ArrayList<HomeSectionEntity>>
}