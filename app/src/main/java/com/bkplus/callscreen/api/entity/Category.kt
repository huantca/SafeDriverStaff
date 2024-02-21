package com.bkplus.callscreen.api.entity


import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
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
    var selected: Boolean = false,

    var type: Int = LatestAdapter.ITEM,
    var nativeAd: BkNativeAd? = null
)
