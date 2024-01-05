package com.bkplus.callscreen.api.entity


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("animatation_url")
    @PrimaryKey(autoGenerate = true)
    val animatationUrl: String? = null,
    @SerializedName("category_id")
    val categoryId: String? = null,
    @SerializedName("font_family")
    val fontFamily: String? = null,
    @SerializedName("free")
    val free: Boolean = false,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("sound_url")
    val soundUrl: String? = null,
    @SerializedName("text_color")
    val textColor: String? = null,
    @SerializedName("text_size")
    val textSize: Int = 0,
    @SerializedName("thumb_url")
    val thumbUrl: String? = null,
    @SerializedName("type")
    val type: String? = null
)