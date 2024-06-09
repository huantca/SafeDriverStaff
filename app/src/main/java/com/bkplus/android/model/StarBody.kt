package com.bkplus.android.model

import com.google.gson.annotations.SerializedName

data class StarBody (
    @SerializedName("status")
    val status: Int = 200,
    @SerializedName("message")
    val message: String?= null,
    @SerializedName("data")
    val data: Double ?= null
)