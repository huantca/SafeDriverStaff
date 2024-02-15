package com.bkplus.callscreen.common

import android.app.Application
import com.ads.bkplus_ads.core.BkPlusAdmobApplication
import com.bkplus.callscreen.ads.TrackingManager
import com.harison.core.app.utils.timber.CrashlyticsTree
import com.harison.core.app.utils.timber.MyDebugTree
import com.harrison.myapplication.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : BkPlusAdmobApplication() {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()

        BasePrefers.initPrefs(applicationContext)
        fetchRemoteConfig()
        setupTimber()
        TrackingManager.init(this)
    }

    private fun fetchRemoteConfig() {
        firebaseRemoteConfig.initRemoteConfig()
        firebaseRemoteConfig.fetchRemoteConfig()
    }

    /** Setup Timber Log*/
    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            val logDebugTree = MyDebugTree(this, false)
            logDebugTree.deleteFileLogFolder(this)
            Timber.plant(logDebugTree)
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }
}
