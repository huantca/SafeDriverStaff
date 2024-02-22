package com.bkplus.callscreen.ui.splash

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdConsentCallback
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdConsent
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.widget.ProgressBarAnimation
import com.google.android.gms.ads.LoadAdError
import com.google.android.ump.FormError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_splash

    @Inject
    lateinit var adsContainer: AdsContainer

    override fun setupUI() {
        super.setupUI()
        startSplash()
        val animation = ProgressBarAnimation(
            binding.progressBar,
            1f,
            100f
        ) {
            //on done splash
        }
        animation.duration = 3000L
        binding.progressBar.startAnimation(animation)
    }

    private fun startSplash() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                delay(3000)
                checkCMP()
            }
        }
    }

    private fun checkCMP() {
        activity?.let {
            BkPlusAdConsent.setupConsent(object : BkPlusAdConsentCallback {
                override fun activity(): Activity {
                    return it
                }

                override fun isDebug(): Boolean {
                    return BuildConfig.DEBUG
                }

                override fun isUnderAgeAd(): Boolean {
                    return false
                }

                override fun onNotUsingAdConsent() {
                    Timber.tag("BkPlusConsent").d("onNotUsingAdConsent")
                    showInterSplash()
                }

                override fun onFormComplete() {
                    Timber.tag("BkPlusConsent").d("onFormComplete: ${BkPlusAdConsent.isCMPConsented(context)}")
                    showInterSplash()
                }

                override fun onFormError(formError: FormError) {
                    Timber.tag("BkPlusConsent").e(formError.message)
                    showInterSplash()
                }
            })
        }
    }

    private fun showInterSplash() {
        preloadNativeLanguage()
        preloadNativeOnboarding()
        if (BasePrefers.getPrefsInstance().inter_splash) {
            BkPlusAdmob.showAdInterstitialSplash(activity, BuildConfig.inter_splash, object :
                BkPlusAdmobInterstitialCallback() {
                override fun onShowAdRequestProgress(tag: String, message: String) {
                    super.onShowAdRequestProgress(tag, message)
                    navigateNextScreen()
                }

                override fun onAdFailed(tag: String, errorMessage: String) {
                    super.onAdFailed(tag, errorMessage)
                    toast(errorMessage)
                }
            })
        } else {
            navigateNextScreen()
        }
    }

    private fun preloadNativeOnboarding() {
        if (BasePrefers.getPrefsInstance().newUser && BasePrefers.getPrefsInstance().native_language) {
            Timber.d("preloadNativeOnboarding()")
            for (i in 0 until 4) {
                BkPlusNativeAd.loadNativeAd(
                    activity,
                    BuildConfig.native_onbroading,
                    R.layout.native_onboarding,
                    object : BkPlusNativeAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            adsContainer.saveNativeAdResponse(nativeAd)
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            super.onAdFailedToLoad(error)
                            adsContainer.saveNativeAdResponse(error)
                        }
                    })
            }
        }
    }

    private fun preloadNativeLanguage() {
        if (BasePrefers.getPrefsInstance().newUser && BasePrefers.getPrefsInstance().native_language) {
            Timber.d("preloadNativeLanguage()")
            BkPlusNativeAd.loadNativeAd(
                activity,
                BuildConfig.native_language,
                R.layout.native_first_language,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        adsContainer.nativeFirstLanguage.postValue(nativeAd)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        adsContainer.nativeFirstLanguage.postValue(error)
                    }
                })
        }
    }

    private fun navigateNextScreen() {
        val newUser = BasePrefers.getPrefsInstance().newUser
        val doneOnboard = BasePrefers.getPrefsInstance().doneOnboard
        val doneWelcome = BasePrefers.getPrefsInstance().doneWelcome
        findNavController().navigate(
            if (newUser) R.id.firstLanguageFragment
            else if (!doneOnboard) R.id.onboardFragment
            else if (!doneWelcome) R.id.welcomeFragment
            else R.id.homeFragment
        )
    }
}
