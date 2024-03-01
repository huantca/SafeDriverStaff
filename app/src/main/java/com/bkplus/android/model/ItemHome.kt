package com.bkplus.android.model

import com.ads.bkplus_ads.core.model.BkNativeAd

data class ItemHome(
    val name: String = "",
    var listItem: List<Item> = listOf(),
    var ads: BkNativeAd? = null,
    var type: Int = 0
)
