package com.bkplus.callscreen.ui.splash

import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.widget.ProgressBarAnimation
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_splash

    override fun setupUI() {
        super.setupUI()
        showInterSplash()
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

    private fun showInterSplash() {
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
    }

    private fun navigateNextScreen() {
        findNavController().navigate(R.id.home_nav_fragment)
        if (BasePrefers.getPrefsInstance().newUser) {

        }
    }
}