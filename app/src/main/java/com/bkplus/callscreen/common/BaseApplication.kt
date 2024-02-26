package com.bkplus.callscreen.common

import com.ads.bkplus_ads.core.BkPlusAdmobApplication
import com.bkplus.callscreen.MainActivity
import com.bkplus.callscreen.ads.TrackingManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.harison.core.app.utils.timber.CrashlyticsTree
import com.harison.core.app.utils.timber.MyDebugTree
import com.harrison.myapplication.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : BkPlusAdmobApplication() {

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

    override fun getAppOpenWhiteList(): List<Class<*>> {
        return listOf(MainActivity::class.java)
    }

    override fun getAdOpenAppUnitId(): String {
        return BuildConfig.appopen_resume
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
