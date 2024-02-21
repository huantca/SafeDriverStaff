package com.bkplus.callscreen.ui.splash.welcome

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_welcome

    private val viewModel: WelcomeViewModel by viewModels()
    private lateinit var adapter: WelcomeAdapter

    override fun setupUI() {
        super.setupUI()
        context?.let {
            binding.continueButton.apply {
                backgroundTintList =
                    ContextCompat.getColorStateList(it, R.color.naviColor1)
                setTextColor(ContextCompat.getColorStateList(it, R.color.naviColor4))
                isEnabled = false
            }
        }
        adapter = WelcomeAdapter().apply {
            setOnItemClickListener {
                context?.let {
                    if (this.hasSelectedItem()) {
                        binding.continueButton.apply {
                            backgroundTintList = ContextCompat.getColorStateList(it, R.color.primary100)
                            elevation = 5f
                            setTextColor(ContextCompat.getColorStateList(it, R.color.white))
                            isEnabled = true
                        }
                    } else {
                        binding.continueButton.apply {
                            backgroundTintList =
                                ContextCompat.getColorStateList(it, R.color.naviColor1)
                            setTextColor(ContextCompat.getColorStateList(it, R.color.naviColor4))
                            isEnabled = false
                        }
                    }
                }
            }
        }
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
        binding.rv.adapter = adapter
        viewModel.fetchCategories()
        loadNativeAd()
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            continueButton.setOnClickListener {
                if (adapter.hasSelectedItem()) {
                    goToHome()
                }
            }
        }
    }

    private fun goToHome() {
        BasePrefers.getPrefsInstance().doneWelcome = true
        findNavController().navigate(R.id.homeFragment)
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_welcome) {
            BkPlusNativeAd.showNativeAdReload(
                this,
                BuildConfig.native_welcome,
                R.layout.native_onboarding,
                binding.flAdplaceholderActivity,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        binding.shimmerContainerNative1.stopShimmer()
                        binding.shimmerContainerNative1.visibility = View.GONE
                        binding.flAdplaceholderActivity.visibility = View.VISIBLE
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        removeNativeAds()
                    }
                }
            )
        } else {
            removeNativeAds()
        }
    }

    private fun removeNativeAds() {
        binding.shimmerContainerNative1.stopShimmer()
        binding.shimmerContainerNative1.visibility = View.GONE
        binding.flAdplaceholderActivity.visibility = View.GONE
    }
}
