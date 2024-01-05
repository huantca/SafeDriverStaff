package com.bkplus.callscreen.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bkplus.callscreen.ultis.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.harrison.myapplication.BuildConfig
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
                initAdsIdPrefs(config)

                val app_version_force_update = config.getString(Constants.app_version_force_update)
                val app_version_latest = config.getString(Constants.app_version_latest)

                BasePrefers.getPrefsInstance().app_version_force_update = app_version_force_update
                BasePrefers.getPrefsInstance().app_version_latest = app_version_latest
            }
            _initAdsComplete.postValue(true)
        }
    }

    private fun initIsShowAdPrefs(config: FirebaseRemoteConfig) {
        val inter_splash = config.getBoolean(Constants.inter_splash)
        val native_language = config.getBoolean(Constants.native_language)
        val native_onboard = config.getBoolean(Constants.native_onboard)
        val banner_home = config.getBoolean(Constants.banner_home)
        val inter_after_onboarding = config.getBoolean(Constants.inter_after_onboarding)
        val open_resume = config.getBoolean(Constants.open_resume)

    }

    data class IdAds(
        val inter_splash: String,
        val native_language: String,
        val native_onboard: String,
        val banner_home: String,
        val inter_after_onboarding: String,
        val native_list_wifi: String,
        val inter_back_to_home: String,
        val native_generator_pass: String,
        val native_speed_test: String,
        val native_connected_devices: String,
        val native_signal_strength: String,
        val native_show_password: String,
        val banner_live_location: String,
        val native_wifi_timer: String,
        val banner_wifi_location: String,
        val native_device_information: String,
        val open_resume: String,
        val reward_show_password: String,
    )

    private fun initAdsIdPrefs(config: FirebaseRemoteConfig) {
        val idAds = try {
            if (BuildConfig.DEBUG) {
                return
            } else {
                Gson().fromJson(config.getString(Constants.id_ads_product), IdAds::class.java)
            }
        } catch (e: Exception) {
            null
        }

        idAds?.apply {
            BasePrefers.getPrefsInstance().id_inter_splash = inter_splash
            BasePrefers.getPrefsInstance().id_native_language = native_language
            BasePrefers.getPrefsInstance().id_native_onboard = native_onboard
            BasePrefers.getPrefsInstance().id_open_resume = open_resume
        }
    }
}
