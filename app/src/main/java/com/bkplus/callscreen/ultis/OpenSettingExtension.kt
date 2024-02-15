package com.bkplus.callscreen.ultis

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager

fun Context?.openPickWifiSetting() {
    this?.startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
}