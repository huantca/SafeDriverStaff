package com.bkplus.callscreen.api.entity


import com.google.gson.annotations.SerializedName

data class HomeSectionEntity(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("wallpapers")
    val items: List<Item>? = null
)