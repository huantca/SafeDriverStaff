package com.bkplus.android.ui.splash

import android.animation.ValueAnimator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ui.widget.ProgressBarAnimation
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.visible
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

        val words = getString(R.string.app_name).split(" ")
        val valueAnimator = ValueAnimator.ofInt(0, words.size)
        valueAnimator.duration = 500 // Thời gian hiển thị mỗi từ

        valueAnimator.addUpdateListener { animation ->
            val index = animation.animatedValue as Int
            binding.tv4k.text = words.subList(0, index ).joinToString(" ")
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                delay(500)
                binding.logo1.gone()
                binding.logo2.visible()
                binding.logo2.animate()
                    .setDuration(500)
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .withEndAction {
                        // Thu nhỏ icon
                        binding.logo2.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .rotationY(360f)
                            .setDuration(500)
                            .start()
                    }
                    .start()
                delay(1000)
                binding.logo2.gone()
                binding.rltContainer.visible()
                binding.imageView2.setImageResource(R.drawable.bg_splash_2)
                valueAnimator.start()
                delay(1000)
                navigateNextScreen()
            }
        }
    }


    private fun navigateNextScreen() {
        val newUser = BasePrefers.getPrefsInstance().newUser
        val doneLogin = BasePrefers.getPrefsInstance().newLogin
        val isDriver = BasePrefers.getPrefsInstance().infoDriver != null
        findNavController().navigate(
            if (newUser) R.id.firstLanguageFragment
            else if (!doneLogin) R.id.loginUserFragment
            else if (isDriver) R.id.driverFragment
            else R.id.userFragment
        )
    }
}
