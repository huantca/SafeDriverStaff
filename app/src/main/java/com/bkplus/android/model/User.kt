package com.bkplus.android.model


data class User(
    val id: Long? = null,
    val name: String? = null,
    val age: Int? = null,
    val email: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val avatar: String? = null,
    val number_of_rentals: Int? = null,
    val trips: List<Trip>? = null,
    val otp: String? = null,
    val otpRequestedTime: Long? = null,
    val createdTime: Long? = null,
    val lastModified: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)