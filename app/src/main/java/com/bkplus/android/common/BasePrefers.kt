package com.bkplus.android.common

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.bkplus.android.model.Driver
import com.bkplus.android.model.Item
import com.bkplus.android.model.Trip
import com.bkplus.android.model.User
import com.bkplus.android.ultis.Constants
import com.bkplus.android.ultis.GsonUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harrison.myapplication.BuildConfig

class BasePrefers(private val context: Context) {

    private val prefsNewUser = "prefsNewUser${BuildConfig.VERSION_NAME}"
    private val prefsNewLogin = "prefsNewLogin"
    private val prefsOnBoard = "prefsOnBoard${BuildConfig.VERSION_NAME}"
    private val prefsWelcome = "prefsWelcome${BuildConfig.VERSION_NAME}"
    private val prefsLocale = "prefsLocale"
    private val prefsInfoTrip = "prefsInfoTrip"
    private val prefsTimer = "prefsTimer"
    private val prefsInfoUserLogin = "prefsInfoUserLogin"
    private val prefsInfoDriverLogin = "prefsInfoDriverLogin"
    private val prefsListThemeFreeHome = "prefsListThemeFreeHome"
    private val prefsModelVehicleUser = "prefsModelVehicleUser"
    private val prefsRangeOfVehicleUser = "prefsRangeOfVehicleUser"

    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    var app_version_force_update
        get() = mPrefs.getString(
            getPrefName(Constants.app_version_force_update),
            BuildConfig.VERSION_NAME
        )
        set(value) = mPrefs.edit {
            putString(
                getPrefName(Constants.app_version_force_update),
                value
            )
        }
    var app_version_latest
        get() = mPrefs.getString(
            getPrefName(Constants.app_version_latest),
            BuildConfig.VERSION_NAME
        )
        set(value) = mPrefs.edit { putString(getPrefName(Constants.app_version_latest), value) }

    var newUser
        get() = mPrefs.getBoolean(prefsNewUser, true)
        set(value) = mPrefs.edit { putBoolean(prefsNewUser, value) }

    var newLogin
        get() = mPrefs.getBoolean(prefsNewLogin, false)
        set(value) = mPrefs.edit { putBoolean(prefsNewLogin, value) }

    var doneOnboard
        get() = mPrefs.getBoolean(prefsOnBoard, false)
        set(value) = mPrefs.edit { putBoolean(prefsOnBoard, value) }

    var doneWelcome
        get() = mPrefs.getBoolean(prefsWelcome, false)
        set(value) = mPrefs.edit { putBoolean(prefsWelcome, value) }


    var requestTrip: Trip?
        set(value) = mPrefs.edit { putString(prefsInfoTrip, GsonUtils.saveObject(value)).apply() }
        get() = GsonUtils.fromJsonSafe<Trip>(mPrefs.getString(prefsInfoTrip, ""))

    var vehicleModelUser
        get() = mPrefs.getString(prefsModelVehicleUser, null)
        set(value) = mPrefs.edit { putString(prefsModelVehicleUser, value) }

    var rangeOfVehicleUser
        get() = mPrefs.getString(prefsRangeOfVehicleUser, null)
        set(value) = mPrefs.edit { putString(prefsRangeOfVehicleUser, value) }

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

    var infoUser: User?
        set(value) = mPrefs.edit { putString(prefsInfoUserLogin, GsonUtils.saveObject(value)).apply() }
        get() = GsonUtils.fromJsonSafe<User>(mPrefs.getString(prefsInfoUserLogin, null))

    var infoDriver : Driver?
        set(value) = mPrefs.edit { putString(prefsInfoDriverLogin, GsonUtils.saveObject(value)).apply() }
        get() = GsonUtils.fromJsonSafe<Driver>(mPrefs.getString(prefsInfoDriverLogin, null))


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
