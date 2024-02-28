package com.bkplus.callscreen.ads

import android.content.Context
import android.os.Bundle
import com.ads.bkplus_ads.core.adjust_sdk.AdjustTracking
import com.google.firebase.analytics.FirebaseAnalytics

object TrackingManager {
    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun tracking(event: EventTracking, bundle: Bundle? = null) {
        firebaseAnalytics?.logEvent(event.name, bundle)
        event.adjustToken?.let { AdjustTracking.trackingRevenue(it) }
    }

    fun trackingString(event: EventTracking, trackingParam: String, value: String) {
        val bundle = Bundle()
        bundle.putString(trackingParam, value)
        firebaseAnalytics?.logEvent(event.name, bundle)
        event.adjustToken?.let { AdjustTracking.trackingRevenue(it) }
    }

    /**Sample tracking: fb003_category_view_{category_name}*/
    fun trackingString(event: EventTracking, param: String) {
        firebaseAnalytics?.logEvent(event.name + param, null)
        event.adjustToken?.let { AdjustTracking.trackingRevenue(it) }
    }

}