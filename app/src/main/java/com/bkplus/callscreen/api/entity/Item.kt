package com.bkplus.callscreen.api.entity


import androidx.room.PrimaryKey
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("loves")
    val loves: Int? = null,
    @SerializedName("free")
    var free: Boolean? = null,
    var isLiked: Boolean = false,


    var type: Int = LatestAdapter.ITEM,
    var nativeAd: BkNativeAd? = null
)
