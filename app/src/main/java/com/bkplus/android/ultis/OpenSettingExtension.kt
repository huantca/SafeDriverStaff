package com.bkplus.android.ultis

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import com.harrison.myapplication.R

fun Context?.openPickWifiSetting() {
    this?.startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
}

fun Activity?.goToFeedback(email: String? = null) {
    if (this != null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, email ?: arrayOf("Trustedapp.help@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, buildString {
                append("Feedback")
                append(getString(R.string.app_name))
            })
            putExtra(Intent.EXTRA_TEXT, "Feedback")
        }
        startActivity(Intent.createChooser(intent, "Feedback"))
    }
}

fun Activity?.openAppInPlayStore() {
    if (this != null) {
        val uri = Uri.parse(Constants.store_uri)
        val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)

        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        goToMarketIntent.addFlags(flags)
        try {
            startActivity(goToMarketIntent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(Constants.LINK_STORE)
            )
            startActivity(intent)
        }
    }
}

fun Activity?.openShare() {
    if (this != null) {
        val text = buildString {
            append(getString(R.string.your_friend))
            append(getString(R.string.app_name))
            append(getString(R.string.visit_app))
            append(Constants.LINK_STORE)
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "plain/text"
        startActivity(
            Intent.createChooser(intent, getString(R.string.choose_way_share))
        )
    }
}