package com.bkplus.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evaluate (
    private val id: Long? = null,
    private val point: Int? = null,
    private val comment: String? = null,
    private val trip: Trip? = null
): Parcelable