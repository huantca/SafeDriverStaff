package com.bkplus.android.api.entity


import com.google.gson.annotations.SerializedName

data class HomeSectionEntity(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("data")
    val data: List<ItemEntity>? = null
)