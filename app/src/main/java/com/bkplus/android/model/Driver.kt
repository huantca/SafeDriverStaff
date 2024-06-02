package com.bkplus.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Driver(
    val id: Long? = null,
    val name: String? = null,
    val age: Int? = null,
    val email: String? = null,
    var phone: String? = null,
    val password: String? = null,
    val address: String? = null,
    val trips: List<Trip>? = null,
    val avatar: String? = null,
    val number_of_times_hired: Int? = null,
    val id_number: Int? = null,
    val id_card_front_photo: String? = null,
    val id_card_back_photo: String? = null,
    val driving_experience: Int? = null,
    val activity_area: String? = null,
    val service_prices: Double? = null,
    val photo_of_health_certificate: String? = null,
    val health_type: String? = null,
    val resume_photo: String? = null,
    val working_time: Long? = null,
    val isAccept: Boolean? = null,
    val vote_number: Int? = 1,
    val star_number: Double? = null
) : Parcelable