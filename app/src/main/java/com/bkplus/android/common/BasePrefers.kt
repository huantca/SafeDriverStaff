package com.bkplus.android.common

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
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
