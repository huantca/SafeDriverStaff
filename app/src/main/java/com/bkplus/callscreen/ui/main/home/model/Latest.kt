package com.bkplus.callscreen.ui.main.home.model

import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter

data class Latest(
    val url : String? = null,
    val category: String?= null,
    val loves : Int?= null,
    val free: Boolean?= null,
    var nativeAd : BkNativeAd ?= null,
    val type : Int?=  LatestAdapter.ITEM
)