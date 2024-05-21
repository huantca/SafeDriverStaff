package com.bkplus.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class StatusE{
    WAIT,
    CONFIRM,
    MOVING,
    CANCEL,
    COMPLETE
}

enum class RangeVehicle{
   CAR,MOTORBIKE,BICYCLE
}

enum class TypeVehicle{
  AUTO,CONTROL
}

@Parcelize
data class Trip (
    val id: Long? = null,
    var date_of_hire: Long? = null,
    var time_start: Long? = null,
    val time_end: Long? = null,
    var pick_up_location: String? = null,
    var pick_up_location_latitude: Double?= null,
    var pick_up_location_longitude : Double?= null,
    var drop_off_location: String? = null,
    var drop_off_location_latitude: Double?= null,
    var drop_off_location_longitude : Double?= null,
    var fee: Double? = null,
    var license_plate: String? = null,
    var car_name: String? = null,
    var vehicle_type: String? = null,
    var range_of_vehicle: String? = null,
    var note: String? = null,
    var status: StatusE? = null,
    var user: User? = null,
    var driver: Driver? = null,
    var evaluates: List<Evaluate>? = null,
    var km : Double?= null,
    var hourly_rental: Int?= 0
): Parcelable