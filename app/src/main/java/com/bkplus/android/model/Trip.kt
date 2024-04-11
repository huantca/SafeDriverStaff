package com.bkplus.android.model

enum class StatusE{
    WAIT,
    CONFIRM,
    MOVING,
    CANCEL,
    COMPLETE
}
data class Trip (
    val id: Long? = null,
    val date_of_hire: Long? = null,
    val time_start: Long? = null,
    val time_end: Long? = null,
    val pick_up_location: String? = null,
    val pick_up_location_latitude: Double?= null,
    val pick_up_location_longitude : Double?= null,
    val drop_off_location: String? = null,
    val drop_off_location_latitude: Double?= null,
    val drop_off_location_longitude : Double?= null,
    val fee: Double? = null,
    val license_plate: String? = null,
    val car_name: String? = null,
    val vehicle_type: String? = null,
    val range_of_vehicle: String? = null,
    val note: String? = null,
    var status: StatusE? = null,
    val user: User? = null,
    var driver: Driver? = null,
    val evaluates: List<Evaluate>? = null,
)