package com.bkplus.callscreen.ui.main.home.model

import com.ads.bkplus_ads.core.model.BkNativeAd

data class Latest(
    val background : String? = null,
    var nativeAd : BkNativeAd ?= null,
    val type : Int?=  0
)