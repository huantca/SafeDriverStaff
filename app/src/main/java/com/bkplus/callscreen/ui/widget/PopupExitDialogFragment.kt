package com.bkplus.callscreen.ui.widget

import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.harison.core.app.platform.BaseDialogFragment
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutDialogExitBinding

class PopupExitDialogFragment : BaseDialogFragment<LayoutDialogExitBinding>() {

    override val layoutId: Int
        get() = R.layout.layout_dialog_exit

    var actionYes = {}
    override fun setupUI() {
        super.setupUI()
        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
        loadNativeAd()
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnExitYes.setOnSingleClickListener {
                dismiss()
                actionYes.invoke()
            }

            btnExitNo.setOnSingleClickListener {
                dismiss()
            }
        }
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_exit) {
            BuildConfig.native_exit.let {
                BkPlusNativeAd.loadNativeAd(
                    this,
                    it,
                    R.layout.native_popup_exit,
                    object : BkPlusNativeAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            populateNativeAd(nativeAd)
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            super.onAdFailedToLoad(error)
                            removeNativeAd()
                        }
                    })
            }
        } else {
            removeNativeAd()
        }

    }

    private fun populateNativeAd(nativeAd: BkNativeAd) {
        binding.shimmerContainerNativeExit.gone()
        binding.shimmerContainerNativeExit.stopShimmer()
        binding.flAdplaceholderExit.visible()
        BkPlusNativeAd.populateNativeAd(this, nativeAd, binding.flAdplaceholderExit)
    }

    private fun removeNativeAd() {
        binding.shimmerContainerNativeExit.stopShimmer()
        binding.shimmerContainerNativeExit.gone()
        binding.frNativeAdsExit.gone()
    }
}