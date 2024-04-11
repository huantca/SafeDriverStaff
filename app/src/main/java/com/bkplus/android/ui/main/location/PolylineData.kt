package com.bkplus.android.ui.main.location

import com.google.android.gms.maps.model.Polyline
import com.google.maps.model.DirectionsLeg


data class PolylineData(
     var polyline: Polyline? = null,
     var leg: DirectionsLeg? = null
) {
    override fun toString(): String {
        return "PolylineData{" +
                "polyline=" + polyline +
                ", leg=" + leg +
                '}'
    }
}