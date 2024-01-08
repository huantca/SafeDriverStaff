package com.bkplus.callscreen.api.calladapter

import com.bkplus.callscreen.api.BaseResponse
import java.io.IOException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResultCall<T : Any>(
    private val proxy: Call<T>,
) : Call<BaseResponse<T>> {
    override fun enqueue(callback: Callback<BaseResponse<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResultCall,
                            Response.success(BaseResponse.Success(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                            this@NetworkResultCall,
                            Response.success(BaseResponse.UnknownError(null))
                        )
                    }
                } else {
                    if (error != null) {
                        callback.onResponse(
                            this@NetworkResultCall,
                            Response.success(
                                BaseResponse.Failure(
                                    code,
                                    error.toString()
                                )
                            )
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResultCall,
                            Response.success(
                                BaseResponse.UnknownError(
                                    null
                                )
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val a = BaseResponse.UnknownError(throwable)
                val networkResponse = when (throwable) {
                    is IOException -> BaseResponse.UnknownError(
                        throwable
                    )

                    else -> BaseResponse.UnknownError(
                        throwable
                    )
                }
                callback.onResponse(this@NetworkResultCall, Response.success(networkResponse))
            }
        })
    }

    override fun execute(): Response<BaseResponse<T>> = throw NotImplementedError()
    override fun clone(): Call<BaseResponse<T>> = NetworkResultCall(proxy.clone())
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() {
        proxy.cancel()
    }
}