package com.bkplus.android.common

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.ads.bkplus_ads.core.callforward.BkPlusAdConsent
import com.bkplus.android.model.Item
import com.bkplus.android.ultis.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harrison.myapplication.BuildConfig

class BasePrefers(private val context: Context) {

    private val prefsNewUser = "prefsNewUser${BuildConfig.VERSION_NAME}"
    private val prefsOnBoard = "prefsOnBoard${BuildConfig.VERSION_NAME}"
    private val prefsWelcome = "prefsWelcome${BuildConfig.VERSION_NAME}"
    private val prefsLocale = "prefsLocale"
    private val prefsTimer = "prefsTimer"

    private val prefsBanner = "prefsBanner"
    private val prefsInterSplash = "prefsInterSplash"
    private val prefsNativeLanguage = "prefsNativeLanguage"
    private val prefsNativeWelcome = "prefsNativeWelcome"
    private val prefsNativeHome = "prefsNativeHome"
    private val prefsNativeOnboard = "prefsNativeOnboard"
    private val prefsInterOnboard = "prefsInterOnboard"
    private val prefsOpenResume = "prefsOpenResume"
    private val prefsListThemeFreeHome = "prefsListThemeFreeHome"

    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    var app_version_force_update
        get() = mPrefs.getString(getPrefName(Constants.app_version_force_update), BuildConfig.VERSION_NAME)
        set(value) = mPrefs.edit { putString(getPrefName(Constants.app_version_force_update), value) }
    var app_version_latest
        get() = mPrefs.getString(getPrefName(Constants.app_version_latest), BuildConfig.VERSION_NAME)
        set(value) = mPrefs.edit { putString(getPrefName(Constants.app_version_latest), value) }

    var newUser
        get() = mPrefs.getBoolean(prefsNewUser, true)
        set(value) = mPrefs.edit { putBoolean(prefsNewUser, value) }

    var doneOnboard
        get() = mPrefs.getBoolean(prefsOnBoard, false)
        set(value) = mPrefs.edit { putBoolean(prefsOnBoard, value) }

    var doneWelcome
        get() = mPrefs.getBoolean(prefsWelcome, false)
        set(value) = mPrefs.edit { putBoolean(prefsWelcome, value) }

    var locale
        get() = mPrefs.getString(prefsLocale, "en")
        set(value) = mPrefs.edit { putString(prefsLocale, value) }

    var appopen_resume
        get() = mPrefs.getBoolean(getPrefName(Constants.appopen_resume), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.appopen_resume), value) }
    var inter_splash
        get() = mPrefs.getBoolean(getPrefName(Constants.inter_splash), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.inter_splash), value) }
    var native_language
        get() = mPrefs.getBoolean(getPrefName(Constants.native_language), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_language), value) }
    var native_onbroading
        get() = mPrefs.getBoolean(getPrefName(Constants.native_onbroading), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_onbroading), value) }
    var native_welcome
        get() = mPrefs.getBoolean(getPrefName(Constants.native_welcome), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_welcome), value) }
    var Banner_all
        get() = mPrefs.getBoolean(getPrefName(Constants.Banner_all), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.Banner_all), value) }
    var Banner_home_collapsible
        get() = mPrefs.getBoolean(getPrefName(Constants.Banner_home_collapsible), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.Banner_home_collapsible), value) }
    var Native_home
        get() = mPrefs.getBoolean(getPrefName(Constants.Native_home), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.Native_home), value) }
    var Native_toptrending
        get() = mPrefs.getBoolean(getPrefName(Constants.Native_toptrending), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.Native_toptrending), value) }
    var native_categories
        get() = mPrefs.getBoolean(getPrefName(Constants.native_categories), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_categories), value) }
    var native_viewcategories
        get() = mPrefs.getBoolean(getPrefName(Constants.native_viewcategories), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_viewcategories), value) }
    var intersitial_backhome
        get() = mPrefs.getBoolean(getPrefName(Constants.intersitial_backhome), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.intersitial_backhome), value) }
    var intersitial_setwallpaper
        get() = mPrefs.getBoolean(getPrefName(Constants.intersitial_setwallpaper), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.intersitial_setwallpaper), value) }
    var native_viewwallpaper
        get() = mPrefs.getBoolean(getPrefName(Constants.native_viewwallpaper), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_viewwallpaper), value) }
    var reward_gif
        get() = mPrefs.getBoolean(getPrefName(Constants.reward_gif), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.reward_gif), value) }
    var intersitial_viewhistory
        get() = mPrefs.getBoolean(getPrefName(Constants.intersitial_viewhistory), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.intersitial_viewhistory), value) }
    var native_sucsess
        get() = mPrefs.getBoolean(getPrefName(Constants.native_sucsess), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_sucsess), value) }
    var native_exit
        get() = mPrefs.getBoolean(getPrefName(Constants.native_exit), true) && BkPlusAdConsent.isCMPConsented(context)
        set(value) = mPrefs.edit { putBoolean(getPrefName(Constants.native_exit), value) }

    var listItemsFree: ArrayList<Item>
        get() = Gson().fromJson(
            mPrefs.getString(
                prefsListThemeFreeHome,
                Gson().toJson(arrayListOf<Item>())
            ), object : TypeToken<ArrayList<Item>>() {}.type
        )
        set(value) = mPrefs.edit { putString(prefsListThemeFreeHome, Gson().toJson(value)).apply() }

    companion object {
        @Volatile
        private var INSTANCE: BasePrefers? = null

        fun initPrefs(context: Context): BasePrefers {
            return INSTANCE ?: synchronized(this) {
                val instance = BasePrefers(context)
                INSTANCE = instance
                // return instance
                instance
            }
        }

        fun getPrefsInstance(): BasePrefers {
            return INSTANCE ?: error("GoPreferences not initialized!")
        }
    }

    private fun getPrefName(name: String): String {
        return "pref$name"
    }
}
