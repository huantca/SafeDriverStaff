package com.bkplus.android.model

import com.google.gson.annotations.SerializedName

data class UserBody(
    @SerializedName("status")
    val status: Int = 200,
    @SerializedName("error")
    val error: String ? = null,
    @SerializedName("message")
    val message: String?= null,
    @SerializedName("data")
    val data: User ?= null
)
