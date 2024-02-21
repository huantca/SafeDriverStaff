package com.bkplus.callscreen.common

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.bkplus.callscreen.ultis.Constants
import com.harrison.myapplication.BuildConfig
import java.util.Calendar

class BasePrefers(context: Context) {

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
        get() = mPrefs.getBoolean(prefsOnBoard, false)
        set(value) = mPrefs.edit { putBoolean(prefsOnBoard, value) }

    var scheduled
        get() = mPrefs.getLong(prefsTimer, Calendar.getInstance().timeInMillis)
        set(value) = mPrefs.edit { putLong(prefsTimer, value) }

    var locale
        get() = mPrefs.getString(prefsLocale, "en")
        set(value) = mPrefs.edit { putString(prefsLocale, value) }

    var banner
        get() = mPrefs.getBoolean(prefsBanner, true)
        set(value) = mPrefs.edit { putBoolean(prefsBanner, value) }

    var interSplash
        get() = mPrefs.getBoolean(prefsInterSplash, true)
        set(value) = mPrefs.edit { putBoolean(prefsInterSplash, value) }

    var nativeLanguage
        get() = mPrefs.getBoolean(prefsNativeLanguage, true)
        set(value) = mPrefs.edit { putBoolean(prefsNativeLanguage, value) }
    var nativeHome
        get() = mPrefs.getBoolean(prefsNativeHome, true)
        set(value) = mPrefs.edit { putBoolean(prefsNativeHome, value) }
    var native_welcome
        get() = mPrefs.getBoolean(prefsNativeWelcome, true)
        set(value) = mPrefs.edit { putBoolean(prefsNativeWelcome, value) }

    var nativeOnboard
        get() = mPrefs.getBoolean(prefsNativeOnboard, true)
        set(value) = mPrefs.edit { putBoolean(prefsNativeOnboard, value) }

    var interOnboard
        get() = mPrefs.getBoolean(prefsInterOnboard, true)
        set(value) = mPrefs.edit { putBoolean(prefsInterOnboard, value) }

    var openResume
        get() = mPrefs.getBoolean(prefsOpenResume, true)
        set(value) = mPrefs.edit { putBoolean(prefsOpenResume, value) }

    //ID Ads
    var id_open_resume
        get() = mPrefs.getString(getPrefName(Constants.id_open_resume), BuildConfig.appopen_resume)
        set(value) = mPrefs.edit { putString(getPrefName(Constants.id_open_resume), value) }
    var id_inter_splash
        get() = mPrefs.getString(getPrefName(Constants.id_inter_splash), BuildConfig.inter_splash)
        set(value) = mPrefs.edit { putString(getPrefName(Constants.id_inter_splash), value) }
    var id_native_language
        get() = mPrefs.getString(getPrefName(Constants.id_native_language), BuildConfig.native_language)
        set(value) = mPrefs.edit { putString(getPrefName(Constants.id_native_language), value) }
    var id_native_onboard
        get() = mPrefs.getString(getPrefName(Constants.id_native_onboard), BuildConfig.native_onbroading)
        set(value) = mPrefs.edit { putString(getPrefName(Constants.id_native_onboard), value) }


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
