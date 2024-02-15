package com.bkplus.callscreen.api.entity


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null
)