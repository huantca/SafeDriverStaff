package com.bkplus.callscreen.ultis

import com.harrison.myapplication.BuildConfig


object Constants {
    const val BASE_URL = "https://fb003-4k-wallpaper.ap-south-1.linodeobjects.com/"

    //TIMEOUT
    const val TIME_OUT = 30000L

    const val app_version_force_update = "app_version_force_update"
    const val app_version_latest: String = "app_version_latest"

    const val inter_splash = "inter_splash"
    const val native_language = "native_language"
    const val native_onboard = "native_onboard"
    const val banner_home = "banner_home"
    const val inter_after_onboarding = "inter_after_onboarding"
    const val open_resume = "open_resume"
    const val cmp_enable = "cmp_enable"
    const val store_uri = "market://details?id=${BuildConfig.APPLICATION_ID}"
    const val channel_id = "channel_id"
    const val channel_name = "channel_name"

    const val id_ads_product = "id_ads_product"
    const val id_open_resume = "id_open_resume"
    const val id_inter_splash = "id_inter_splash"
    const val id_native_language = "id_native_language"
    const val id_native_onboard = "id_native_onboard"
    const val request_code_share = 9999

    //URL
    const val LINK_STORE =
        "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}&hl=en_IE"
    const val PRIVACY_POLICY_LINK =
        "https://firebasestorage.googleapis.com/v0/b/bkplus-wifi-master-android.appspot.com/o/Policy.html?alt=media&token=81c8f298-b3ed-444e-99a8-f3e136a9efdf"
    const val TERMS_OF_USE_LINK =
        "https://firebasestorage.googleapis.com/v0/b/bkplus-wifi-master-android.appspot.com/o/TOS.html?alt=media&token=bce3bc1e-46cd-4416-b8fa-832d223ad010"

    //const support language
    const val en = "en"
    const val hi = "hi"
    const val it = "it"
    const val pt = "pt"
    const val de = "de"
    const val zh = "zh"
    const val vi = "vi"
    const val fr = "fr"
    const val es = "es"
    const val ja = "ja"
    const val ko = "ko"
    const val bn = "bn"
    const val mr = "mr"
    const val ru = "ru"
    const val ta = "ta"
    const val te = "te"
    const val th = "th"
    const val tr = "tr"

}
