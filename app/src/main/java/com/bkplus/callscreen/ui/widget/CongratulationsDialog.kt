package com.bkplus.callscreen.ui.widget

import android.view.View
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.google.android.gms.ads.LoadAdError
import com.harison.core.app.platform.BaseFullScreenDialogFragment
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCongratulationsBinding

class CongratulationsDialog: BaseFullScreenDialogFragment<FragmentCongratulationsBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_congratulations

    var actionHome = {}
    var actionBack = {}

    override fun setupUI() {
        super.setupUI()
        loadNativeAd()
        binding.apply {
            context?.let {
                Glide.with(it)
                    .load(R.drawable.gif_congratulation)
                    .into(congratulateGif)
            }
        }
    }
    override fun setupListener() {
        super.setupListener()
        binding.apply {
            backBtn.setOnSingleClickListener {
                actionBack.invoke()
                dismiss()
            }

            homeBtn.setOnSingleClickListener {
                actionHome.invoke()
            }
        }
    }


    private fun loadNativeAd() {
        BkPlusNativeAd.showNativeAdReload(
            this,
            BuildConfig.native_categories,
            R.layout.layout_native_congratulation,
            binding.flAdPlaceholderDeviceInfo,
            object : BkPlusNativeAdCallback() {
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