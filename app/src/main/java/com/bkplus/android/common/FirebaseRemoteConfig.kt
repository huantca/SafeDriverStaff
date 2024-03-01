package com.bkplus.android.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bkplus.android.ultis.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.harrison.myapplication.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfig @Inject constructor() {

    private val _initAdsComplete = MutableLiveData(false)
    val initAdsComplete: LiveData<Boolean> = _initAdsComplete

    fun initRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {}
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
    }

    fun fetchRemoteConfig() {
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
        config.setConfigSettingsAsync(configSettings)
        config.setDefaultsAsync(R.xml.remote_config_defaults)

        config.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {

                initIsShowAdPrefs(config)

                val app_version_force_update = config.getString(Constants.app_version_force_update)
                val app_version_latest = config.getString(Constants.app_version_latest)

                BasePrefers.getPrefsInstance().app_version_force_update = app_version_force_update
                BasePrefers.getPrefsInstance().app_version_latest = app_version_latest
            }
            _initAdsComplete.postValue(true)
        }
    }

    private fun initIsShowAdPrefs(config: FirebaseRemoteConfig) {
        val appopen_resume = config.getBoolean(Constants.appopen_resume)
        val inter_splash = config.getBoolean(Constants.inter_splash)
        val native_language = config.getBoolean(Constants.native_language)
        val native_onbroading = config.getBoolean(Constants.native_onbroading)
        val native_welcome = config.getBoolean(Constants.native_welcome)
        val Banner_all = config.getBoolean(Constants.Banner_all)
        val Native_home = config.getBoolean(Constants.Native_home)
        val Native_toptrending = config.getBoolean(Constants.Native_toptrending)
        val native_categories = config.getBoolean(Constants.native_categories)
        val native_viewcategories = config.getBoolean(Constants.native_viewcategories)
        val intersitial_backhome = config.getBoolean(Constants.intersitial_backhome)
        val intersitial_setwallpaper = config.getBoolean(Constants.intersitial_setwallpaper)
        val native_viewwallpaper = config.getBoolean(Constants.native_viewwallpaper)
        val reward_gif = config.getBoolean(Constants.reward_gif)
        val intersitial_viewhistory = config.getBoolean(Constants.intersitial_viewhistory)
        val native_sucsess = config.getBoolean(Constants.native_sucsess)
        val native_exit = config.getBoolean(Constants.native_exit)

        BasePrefers.getPrefsInstance().appopen_resume = appopen_resume
        BasePrefers.getPrefsInstance().inter_splash = inter_splash
        BasePrefers.getPrefsInstance().native_language = native_language
        BasePrefers.getPrefsInstance().native_onbroading = native_onbroading
        BasePrefers.getPrefsInstance().native_welcome = native_welcome
        BasePrefers.getPrefsInstance().Banner_all = Banner_all
        BasePrefers.getPrefsInstance().Native_home = Native_home
        BasePrefers.getPrefsInstance().Native_toptrending = Native_toptrending
        BasePrefers.getPrefsInstance().native_categories = native_categories
        BasePrefers.getPrefsInstance().native_viewcategories = native_viewcategories
        BasePrefers.getPrefsInstance().intersitial_backhome = intersitial_backhome
        BasePrefers.getPrefsInstance().intersitial_setwallpaper = intersitial_setwallpaper
        BasePrefers.getPrefsInstance().native_viewwallpaper = native_viewwallpaper
        BasePrefers.getPrefsInstance().reward_gif = reward_gif
        BasePrefers.getPrefsInstance().intersitial_viewhistory = intersitial_viewhistory
        BasePrefers.getPrefsInstance().native_sucsess = native_sucsess
        BasePrefers.getPrefsInstance().native_exit = native_exit
    }
}
