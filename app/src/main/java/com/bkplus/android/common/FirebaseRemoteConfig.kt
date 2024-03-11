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

                val app_version_force_update = config.getString(Constants.app_version_force_update)
                val app_version_latest = config.getString(Constants.app_version_latest)

                BasePrefers.getPrefsInstance().app_version_force_update = app_version_force_update
                BasePrefers.getPrefsInstance().app_version_latest = app_version_latest
            }
            _initAdsComplete.postValue(true)
        }
    }

}
