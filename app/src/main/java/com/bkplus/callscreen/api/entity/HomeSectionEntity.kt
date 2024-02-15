package com.bkplus.callscreen.api.entity


import com.google.gson.annotations.SerializedName

data class HomeSectionEntity(
    @SerializedName("categories")
    val categories: List<Category?>? = null,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("items")
    val items: List<Item?>? = null,
    @SerializedName("name")
    val name: String? = null
)