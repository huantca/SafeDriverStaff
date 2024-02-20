package com.bkplus.callscreen.ui.viewlike

data class WallPaper(
    var isLiked: Boolean = false,
    val id: Int,
    val url: String? = null,
    var likes: Int
)