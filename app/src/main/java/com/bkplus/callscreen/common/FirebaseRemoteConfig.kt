package com.harrison.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ads.control.admob.AppOpenManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.harison.core.app.BuildConfig
import com.harison.core.app.ui.main.MainActivity
import com.harison.core.app.utils.BasePrefers
import com.harison.core.app.utils.Constants
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
                val inter_back_to_home_cool_down = config.getLong(Constants.inter_back_to_home_cool_down)

                BasePrefers.getPrefsInstance().app_version_force_update = app_version_force_update
                BasePrefers.getPrefsInstance().app_version_latest = app_version_latest
                BasePrefers.getPrefsInstance().inter_back_to_home_cool_down = inter_back_to_home_cool_down
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
        val native_list_wifi = config.getBoolean(Constants.native_list_wifi)
        val inter_back_to_home = config.getBoolean(Constants.inter_back_to_home)
        val native_generator_pass = config.getBoolean(Constants.native_generator_pass)
        val native_speed_test = config.getBoolean(Constants.native_speed_test)
        val native_connected_devices = config.getBoolean(Constants.native_connected_devices)
        val native_signal_strength = config.getBoolean(Constants.native_signal_strength)
        val native_show_password = config.getBoolean(Constants.native_show_password)
        val banner_live_location = config.getBoolean(Constants.banner_live_location)
        val native_wifi_timer = config.getBoolean(Constants.native_wifi_timer)
        val banner_wifi_location = config.getBoolean(Constants.banner_wifi_location)
        val native_device_information = config.getBoolean(Constants.native_device_information)
        val open_resume = config.getBoolean(Constants.open_resume)
        val reward_show_password = config.getBoolean(Constants.reward_show_password)

        BasePrefers.getPrefsInstance().inter_splash = inter_splash
        BasePrefers.getPrefsInstance().native_language = native_language
        BasePrefers.getPrefsInstance().native_onboard = native_onboard
        BasePrefers.getPrefsInstance().banner_home = banner_home
        BasePrefers.getPrefsInstance().inter_after_onboarding = inter_after_onboarding
        BasePrefers.getPrefsInstance().native_list_wifi = native_list_wifi
        BasePrefers.getPrefsInstance().inter_back_to_home = inter_back_to_home
        BasePrefers.getPrefsInstance().native_generator_pass = native_generator_pass
        BasePrefers.getPrefsInstance().native_speed_test = native_speed_test
        BasePrefers.getPrefsInstance().native_connected_devices = native_connected_devices
        BasePrefers.getPrefsInstance().native_signal_strength = native_signal_strength
        BasePrefers.getPrefsInstance().native_show_password = native_show_password
        BasePrefers.getPrefsInstance().banner_live_location = banner_live_location
        BasePrefers.getPrefsInstance().native_wifi_timer = native_wifi_timer
        BasePrefers.getPrefsInstance().banner_wifi_location = banner_wifi_location
        BasePrefers.getPrefsInstance().native_device_information = native_device_information
        BasePrefers.getPrefsInstance().open_resume = open_resume
        BasePrefers.getPrefsInstance().reward_show_password = reward_show_password

        if (!BasePrefers.getPrefsInstance().open_resume) {
            AppOpenManager.getInstance().disableAppResumeWithActivity(MainActivity::class.java)
        } else {
            AppOpenManager.getInstance().enableAppResumeWithActivity(MainActivity::class.java)
            AppOpenManager.getInstance().enableAppResume()
        }
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
            if (BuildConfig.build_debug) {
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
            BasePrefers.getPrefsInstance().id_banner_home = banner_home
            BasePrefers.getPrefsInstance().id_inter_after_onboarding = inter_after_onboarding
            BasePrefers.getPrefsInstance().id_native_list_wifi = native_list_wifi
            BasePrefers.getPrefsInstance().id_inter_back_to_home = inter_back_to_home
            BasePrefers.getPrefsInstance().id_native_generator_pass = native_generator_pass
            BasePrefers.getPrefsInstance().id_native_speed_test = native_speed_test
            BasePrefers.getPrefsInstance().id_native_connected_devices = native_connected_devices
            BasePrefers.getPrefsInstance().id_native_signal_strength = native_signal_strength
            BasePrefers.getPrefsInstance().id_native_show_password = native_show_password
            BasePrefers.getPrefsInstance().id_banner_live_location = banner_live_location
            BasePrefers.getPrefsInstance().id_native_wifi_timer = native_wifi_timer
            BasePrefers.getPrefsInstance().id_banner_wifi_location = banner_wifi_location
            BasePrefers.getPrefsInstance().id_native_device_information = native_device_information
            BasePrefers.getPrefsInstance().id_open_resume = open_resume
            BasePrefers.getPrefsInstance().id_reward_show_password = reward_show_password
        }
    }
}
