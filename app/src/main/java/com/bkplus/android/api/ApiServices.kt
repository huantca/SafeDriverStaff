package com.bkplus.android.api

import com.bkplus.android.api.entity.HomeSectionEntity
import com.bkplus.android.model.Driver
import com.bkplus.android.model.DriverBody
import com.bkplus.android.model.OtpBody
import com.bkplus.android.model.RequestOtp
import com.bkplus.android.model.StarBody
import com.bkplus.android.model.TripBody
import com.bkplus.android.model.User
import com.bkplus.android.model.UserBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
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

    @POST("driver/login")
    suspend fun loginDriver(
        @Body driver: Driver
    ): BaseResponse<DriverBody>


    @POST("auth/register")
    suspend fun registerUser(
        @Body user: User
    ): BaseResponse<UserBody>

    @POST("auth/request-otp")
    suspend fun sendOtp(
        @Body requestOtp: RequestOtp
    ): BaseResponse<OtpBody>


    @POST("driver/evaluate")
    suspend fun voteDriver(
        @Body driver: Driver,
        @Query("start") start: Int,
    ): BaseResponse<DriverBody>

    @POST("driver/star")
    suspend fun getStar(
        @Query("id") id: Long,
    ): BaseResponse<StarBody>

    @POST("user/update")
    suspend fun saveInfoUser(
        @Body user: User,
    ): BaseResponse<UserBody>

    @Multipart
    @POST("user/avatar")
    suspend fun updateAvatarUser(
        @Part avatar: MultipartBody.Part,
        @Part("user") user: RequestBody,
    ): BaseResponse<UserBody>

    @POST("user/infoUser")
    suspend fun infoUser(
        @Body user: User,
    ): BaseResponse<UserBody>
}
