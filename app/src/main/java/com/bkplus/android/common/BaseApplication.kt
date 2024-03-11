package com.bkplus.android.common

import android.app.Application
import com.bkplus.android.ads.TrackingManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.harison.core.app.utils.timber.CrashlyticsTree
import com.harison.core.app.utils.timber.MyDebugTree
import com.harrison.myapplication.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
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
