package com.bkplus.callscreen.ads

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object TrackingManager {
    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun tracking(event: EventTracking, bundle: Bundle? = null) {
        firebaseAnalytics?.logEvent(event.name, bundle)
    }

    fun trackingString(event: EventTracking, trackingParam: String, value: String) {
        val bundle = Bundle()
        bundle.putString(trackingParam, value)
        firebaseAnalytics?.logEvent(event.name, bundle)
    }

}