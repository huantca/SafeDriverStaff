package com.bkplus.android.model

import com.google.gson.annotations.SerializedName

data class TripBody(
    @SerializedName("status")
    val status: Int = 200,
    @SerializedName("data")
    val data: ArrayList<Trip>? = null
) {

}