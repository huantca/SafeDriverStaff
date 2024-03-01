package com.bkplus.android.model

data class Item(
    val id: Int,
    val url: String? = null,
    var path: String? = null,
    var isLike: Boolean = false,
    var isFree: Boolean = false,
    val category: String? = null
)
