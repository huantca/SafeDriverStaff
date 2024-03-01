package com.bkplus.android.ui.splash.onboard

import android.os.Bundle
import android.view.View
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.ads.EventTracking
import com.bkplus.android.ads.TrackingManager
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.invisible
import com.bkplus.android.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentOnboardChildBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChildOnboardFragment : BaseFragment<FragmentOnboardChildBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_onboard_child

    @Inject
    lateinit var adsContainer: AdsContainer
    private var gotNativeAdResponse = false
    private var stopped = false

    companion object {
        private const val IMAGE_BUNDLE = "IMAGE_BUNDLE"
        private const val TEXT_TITLE = "TEXT_TITLE"
        private const val TEXT_DES = "TEXT_DES"
        fun newInstance(
            image: Int?,
            textTitle: String?,
            textDescription: String?
        ): ChildOnboardFragment {
            val args = Bundle()
            args.putInt(IMAGE_BUNDLE, image ?: R.drawable.ob1)
            args.putString(TEXT_TITLE, textTitle.orEmpty())
            args.putString(TEXT_DES, textDescription.orEmpty())
            val fragment = ChildOnboardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var image = R.drawable.ob1
    private var textTitle = ""
    private var textDescription = ""

    override fun setupData() {
        super.setupData()
        arguments?.let {
            image = it.getInt(IMAGE_BUNDLE)
            textTitle = it.getString(TEXT_TITLE, "")
            textDescription = it.getString(TEXT_DES, "")
        }
    }

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            imageOnboard.setImageResource(image)
            tvTitle.text = textTitle
            tvDescription.text = textDescription
        }
        showNativeAdIfReady()
    }

    override fun onResume() {
        super.onResume()
        reloadNativeAd()
    }

    override fun onStop() {
        super.onStop()
        stopped = true
    }

    /**
     * reload native ad when user return to app (hide app or navigate to others app and comeback)
     */
    private fun reloadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_onbroading && gotNativeAdResponse && stopped) {
            Timber.d("reload native ad")
            binding.flAdplaceholderActivity.removeAllViews()
            binding.shimmerContainerNative1.startShimmer()
            binding.shimmerContainerNative1.visibility = View.VISIBLE
            activity?.let {
                BkPlusNativeAd.loadNativeAd(
                    activity,
                    BuildConfig.native_onbroading,
                    R.layout.native_onboarding,
                    object : BkPlusNativeAdCallback() {
                        override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            populateNativeAd(nativeAd)
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            super.onAdFailedToLoad(error)
                            removeNativeAd()
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            TrackingManager.tracking(EventTracking.fb011_click_ads_onboarding)
                        }
                    }
                )
            }
        }
        stopped = false
    }

    private fun showNativeAdIfReady() {
        if (BasePrefers.getPrefsInstance().native_onbroading) {
            adsContainer.nativeOnboardingAdResponse.observe(viewLifecycleOwner) {
                if (gotNativeAdResponse) return@observe
                when (val nativeAd = adsContainer.getNativeOnboardingResponse()) {
                    is LoadAdError -> removeNativeAd()
                    is BkNativeAd -> populateNativeAd(nativeAd)
                }
            }
        } else {
            removeNativeAd()
        }
    }

    private fun populateNativeAd(nativeAd: BkNativeAd) {
        gotNativeAdResponse = true
        binding.shimmerContainerNative1.gone()
        binding.shimmerContainerNative1.stopShimmer()
        binding.flAdplaceholderActivity.visible()
        BkPlusNativeAd.populateNativeAd(this, nativeAd, binding.flAdplaceholderActivity)
    }

    private fun removeNativeAd() {
        gotNativeAdResponse = true
        binding.shimmerContainerNative1.stopShimmer()
        binding.shimmerContainerNative1.invisible()
        binding.frNativeAds.invisible()
    }
}
