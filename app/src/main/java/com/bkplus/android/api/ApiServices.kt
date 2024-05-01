package com.bkplus.android.api

import com.bkplus.android.api.entity.HomeSectionEntity
import com.bkplus.android.model.Driver
import com.bkplus.android.model.OtpBody
import com.bkplus.android.model.RequestOtp
import com.bkplus.android.model.TripBody
import com.bkplus.android.model.User
import com.bkplus.android.model.UserBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>

    @GET("home-sections.json")
    suspend fun getApiData(): BaseResponse<ArrayList<HomeSectionEntity>>

    @POST("trip/userHistory")
    suspend fun getHistoryUser(
        @Body user: User
    ): BaseResponse<TripBody>

    @POST("trip/driverHistory")
    suspend fun getHistoryDriver(
        @Body driver: Driver
    ): BaseResponse<TripBody>

    @POST("user/login")
    suspend fun loginUser(
        @Body user: User
    ): BaseResponse<UserBody>


    @POST("auth/register")
    suspend fun registerUser(
        @Body user: User
    ): BaseResponse<UserBody>

    @POST("auth/request-otp")
    suspend fun sendOtp(
        @Body requestOtp: RequestOtp
    ): BaseResponse<OtpBody>

}
