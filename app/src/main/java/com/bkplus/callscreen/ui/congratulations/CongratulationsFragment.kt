package com.bkplus.callscreen.ui.congratulations

import android.view.View
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCongratulationsBinding

class CongratulationsFragment: BaseFragment<FragmentCongratulationsBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_congratulations

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            Glide.with(this@CongratulationsFragment)
                .load(R.drawable.gif_congratulation)
                .into(congratulateGif)
        }
    }
    override fun setupListener() {
        super.setupListener()
        binding.apply {
            backBtn.setOnSingleClickListener {
                findNavController().popBackStack()
            }

            homeBtn.setOnSingleClickListener {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }


    private fun loadNativeAd() {
        BkPlusNativeAd.loadNativeAd(
            this,
            BuildConfig.native_categories,
            R.layout.layout_native_congratulation,
            object : BkPlusNativeAdCallback() {
                override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    populateNativeAd(nativeAd)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    removeNativeAd()
                }
            }
        )
    }

    private fun populateNativeAd(nativeAd: BkNativeAd) {
        binding.shimmerContainerNative2.visibility = View.GONE
        binding.shimmerContainerNative2.stopShimmer()
        binding.flAdPlaceholderDeviceInfo.visibility = View.VISIBLE
        BkPlusNativeAd.populateNativeAd(this, nativeAd, binding.flAdPlaceholderDeviceInfo)
    }

    private fun removeNativeAd() {
        binding.shimmerContainerNative2.stopShimmer()
        binding.shimmerContainerNative2.visibility = View.GONE
        binding.frNativeAdsDeviceInfo.visibility = View.GONE
    }

}