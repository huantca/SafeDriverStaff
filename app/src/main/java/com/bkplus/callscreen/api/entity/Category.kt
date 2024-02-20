package com.bkplus.callscreen.api.entity


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("size")
    var number: Int = 0,
    var selected: Boolean = false
)
