package com.bkplus.callscreen.di

import android.content.Context
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.calladapter.NetworkResultCallAdapterFactory
import com.bkplus.callscreen.ultis.Constants.BASE_URL
import com.bkplus.callscreen.ultis.Constants.TIME_OUT
import com.bkplus.callscreen.ultis.NetworkState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor { message -> Timber.tag("----").e("\n" + message) }
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                ongoing.apply {
                    addHeader("Content-Type", "application/x-protobuf")
                    addHeader("Accept", "*/*")
                }
                chain.proceed(ongoing.build())
            }
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor())
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .callTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkState(@ApplicationContext appContext: Context): NetworkState = NetworkState(
        appContext
    )
}