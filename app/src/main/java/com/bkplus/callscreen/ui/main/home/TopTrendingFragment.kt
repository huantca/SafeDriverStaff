package com.bkplus.callscreen.ui.main.home

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.ads.bkplus_ads.core.callforward.BkPlusBannerAd
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.bkplus.callscreen.ui.main.home.search.SearchFragmentDirections
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.harison.core.app.platform.BaseFullScreenDialogFragment
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentTopTrendingBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TopTrendingFragment : BaseFullScreenDialogFragment<FragmentTopTrendingBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer
    override val layoutId: Int
        get() = R.layout.fragment_top_trending

    private var adapter: LatestAdapter? = null
    private var data: ArrayList<Item>? = null
    var dismissDialog: () -> Unit? = {}
    fun setData(data: ArrayList<Item>) {
        this.data = data
    }

    override fun setupData() {
        super.setupData()
        loadInterBackHome()
        adapter = LatestAdapter()
        val arr = ArrayList<Item>()
        data?.forEachIndexed { index, item ->
            val count = index + 1
            arr.add(item)
            if (count % 6 == 0) {
                arr.add(Item(nativeAd = null, type = LatestAdapter.ADS))
            }
        }
        adapter?.updateItems(arr)
        binding.rcyTopTrendy.adapter = adapter
        adapter?.itemAction = actionItem
        showBanner()
        loadNativeAd()
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            icBack.setOnSingleClickListener {
                showInterBackHome {
                    dismiss()
                }
            }
        }
    }


    private val actionItem: (Item) -> Unit = { item ->
        this.dismiss()
        val wallpaper =
            WallPaper(id = item.id, url = item.url, likeCount = item.loves, free = item.free)
        val listItem = data?.map { dataItem ->
            WallPaper(
                id = dataItem.id,
                url = dataItem.url,
                likeCount = dataItem.loves,
                free = wallpaper.free
            )
        }?.toTypedArray()
        listItem?.let {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToViewLikeContainerFragment(
                    R.id.homeFragment,
                    wallpaper,
                    listItem
                )
            )
        }
        dismissDialog.invoke()
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().Native_toptrending) {
            Timber.d("loadNativeAd()")
            BkPlusNativeAd.loadNativeAd(
                this,
                BuildConfig.Native_toptrending,
                R.layout.native_onboarding,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        adapter?.populateNativeAd(nativeAd, this@TopTrendingFragment)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        adapter?.removeNativeAd()
                    }
                }
            )
        } else {
            adapter?.removeNativeAd()
        }
    }

    private fun showBanner() {
        if (BasePrefers.getPrefsInstance().Banner_all) {
            binding.banner.visible()
            BkPlusBannerAd.showAdCollapsibleBanner(
                context,
                BuildConfig.Banner_all,
                binding.banner,
                null
            )
        } else {
            binding.banner.gone()
        }
    }

    private fun loadInterBackHome() {
        if (BasePrefers.getPrefsInstance().intersitial_backhome) {
            activity?.let {
                if (!adsContainer.isInterAdReady(BuildConfig.intersitial_backhome)) {
                    BkPlusAdmob.loadAdInterstitial(it, BuildConfig.intersitial_backhome,
                        object : BkPlusAdmobInterstitialCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                adsContainer.saveInterAd(
                                    BuildConfig.intersitial_backhome,
                                    interstitialAd
                                )
                            }
                        })
                }

            }
        }
    }

    private fun showInterBackHome(action: () -> Unit) {
        if (BasePrefers.getPrefsInstance().intersitial_backhome) {
            activity?.let {
                BkPlusAdmob.showAdInterstitial(it,
                    adsContainer.getInterAd(BuildConfig.intersitial_backhome),
                    object : BkPlusAdmobInterstitialCallback() {
                        override fun onShowAdRequestProgress(tag: String, message: String) {
                            super.onShowAdRequestProgress(tag, message)
                            action.invoke()
                        }

                        override fun onAdFailed(tag: String, errorMessage: String) {
                            super.onAdFailed(tag, errorMessage)
                            adsContainer.removeInterAd(BuildConfig.intersitial_backhome)
                        }

                        override fun onAdDismissed(tag: String, message: String) {
                            super.onAdDismissed(tag, message)
                            adsContainer.removeInterAd(BuildConfig.intersitial_backhome)
                        }
                    })
            }
        } else {
            action.invoke()
        }
    }
}
