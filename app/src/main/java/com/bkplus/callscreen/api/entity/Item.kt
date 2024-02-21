package com.bkplus.callscreen.api.entity


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("loves")
    val loves: Int? = null,
    @SerializedName("free")
    val free: Boolean? = null,
    var isLiked: Boolean = false
)