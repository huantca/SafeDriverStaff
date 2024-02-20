package com.bkplus.callscreen.ui.viewlike

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WallPaper(
    var isLiked: Boolean = false,
    val id: Int? = null,
    val url: String? = null,
    var likes: Int = 0
) : Parcelable