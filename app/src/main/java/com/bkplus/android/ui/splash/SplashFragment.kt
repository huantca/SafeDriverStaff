package com.bkplus.android.ui.splash

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ui.widget.ProgressBarAnimation
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                navigateNextScreen()
            }
        }
    }
    private fun showInterSplash() {
        navigateNextScreen()
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
