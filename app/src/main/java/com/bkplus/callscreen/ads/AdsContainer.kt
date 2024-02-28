package com.bkplus.callscreen.ads

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import timber.log.Timber
import java.util.Stack
import javax.inject.Inject
import javax.inject.Singleton


/**
 * this singleton will be used as a container for [ApInterstitialAd] and [ApRewardAd].
 * Ads will be stored in a [MutableMap] with the key is the ad's id and the value will be the ad associated with the key provided
 * @author Duc An
 */
@Singleton
class AdsContainer @Inject constructor() {
    private val interAdMap = mutableMapOf<String, InterstitialAd?>()
    private val rewardAdMap = mutableMapOf<String, RewardedAd?>()
    private var lastTimeShownInterAd = 0L
    private var interCoolDownInMillis = 0L
    var nativeFirstLanguage = MutableLiveData<Any?>()
    var nativeOnboardingAdResponse = MutableLiveData<Boolean>(false)
    private val stack = Stack<Any>()
    fun setInterCoolDownTime(interCoolDownTimeInSecond: Long) {
        this.interCoolDownInMillis = interCoolDownTimeInSecond * 1000
    }

    @Synchronized
    fun getInterAd(adId: String?): InterstitialAd? {
        if (adId == null) return null
        val canShowInterAd =
            System.currentTimeMillis() - lastTimeShownInterAd > interCoolDownInMillis
        if (canShowInterAd) {
            lastTimeShownInterAd = System.currentTimeMillis()
        } else {
            Timber.tag(this.javaClass.simpleName).d("Interstitial ad is cooling down")
        }
        return if (canShowInterAd) interAdMap[adId] else null
    }

    @Synchronized
    fun isInterAdReady(adId: String): Boolean {
        val interAd = interAdMap[adId]
        return interAd != null
    }

    @Synchronized
    fun saveInterAd(adId: String?, interAd: InterstitialAd?) {
        if (adId == null) {
            Timber.tag(this.javaClass.simpleName)
                .d("Save inter ad failed: Ad id is null")
            return
        }
        if (!isInterAdReady(adId)) {
            interAdMap[adId] = interAd
            Timber.tag(this.javaClass.simpleName)
                .d("An Interstitial ad for id '$adId' is stored")
        } else {
            Timber.tag(this.javaClass.simpleName)
                .w("Interstitial ad for id '$adId' has already existed")
        }
    }

    fun removeInterAd(adId: String) {
        interAdMap.remove(adId)
    }

    @Synchronized
    fun getRewardedAd(adId: String): RewardedAd? {
        return rewardAdMap[adId]
    }

    @Synchronized
    fun isRewardedAdReady(adId: String): Boolean {
        val rewardAd = rewardAdMap[adId]
        return rewardAd != null
    }

    @Synchronized
    fun removeRewardedAd(adId: String) {
        rewardAdMap.remove(adId)
    }

    @Synchronized
    fun saveRewardedAd(adId: String, rewardAd: RewardedAd?) {
        if (!isRewardedAdReady(adId)) {
            rewardAdMap[adId] = rewardAd
            Timber.tag(this.javaClass.simpleName).d("A Rewarded ad for id '$adId' is stored")
        } else {
            Timber.tag(this.javaClass.simpleName)
                .w("Rewarded ad for id '$adId' has already existed")
        }
    }

    @Synchronized
    fun getNativeOnboardingResponse(): Any? {
        return if (stack.isNotEmpty()) stack.pop() else null
    }

    @Synchronized
    fun saveNativeAdResponse(response: Any) {
        Log.d("----","saveNativeAdResponse: ${response.javaClass.simpleName}")
        stack.add(response)
        nativeOnboardingAdResponse.postValue(true)
    }
}
