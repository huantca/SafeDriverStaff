package com.bkplus.callscreen.api.calladapter

import com.bkplus.callscreen.api.BaseResponse
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter

class NetworkResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<BaseResponse<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<BaseResponse<Type>> {
        return NetworkResultCall(call)
    }
}