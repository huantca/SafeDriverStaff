package com.bkplus.android.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("age")
    var age: Int? = null,
    @SerializedName("email")
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    @SerializedName("address")
    var address: String? = null,
    var avatar: String? = null,
    var number_of_rentals: Int? = null,
    var trips: List<Trip>? = null,
    var otp: String? = null,
    var otpRequestedTime: Long? = null,
    var createdTime: Long? = null,
    var lastModified: Long? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null
)