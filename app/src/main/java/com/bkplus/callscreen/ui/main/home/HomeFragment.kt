package com.bkplus.callscreen.ui.main.home

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobRewardedCallback
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.home.adapter.HomeAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ui.widget.ForceUpdateDialog
import com.bkplus.callscreen.ui.widget.RewardDialog
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding
import com.qrcode.ai.app.ui.main.widget.OptionUpdateDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer
    override val layoutId: Int
        get() = R.layout.fragment_home
    private var adapter: HomeAdapter? = null
    private val viewModel: HomeViewModel by activityViewModels()

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

        var data = ArrayList<HomeSectionEntity>()
        var isShowDialogForceUpdate: Boolean? = true
    }

    override fun setupUI() {
        super.setupUI()
        loadAdReward()
        setupShowForceUpdate()
    }

    override fun setupData() {
        super.setupData()
        adapter = HomeAdapter()
        adapter?.onItemRcvClick = { item, listData ->
            if (item.free == true) {
                gotoViewLike(item, listData)
            } else {
                RewardDialog().apply {
                    action = {
                        showRewardAd {
                            viewModel.freeItem(item)
                            gotoViewLike(item, listData)
                        }
                    }
                }.show(childFragmentManager,"")
            }
        }
        adapter?.viewAll = {
            val topTrendingFragment = TopTrendingFragment().apply {
                setData(it)
            }

            topTrendingFragment.dismissDialog = {
                topTrendingFragment.dismiss()
            }
            topTrendingFragment.show(childFragmentManager, "")
        }
        viewModel.homeSectionLiveData.observe(viewLifecycleOwner) {
            adapter?.updateItems(it)
            if (!it.isNullOrEmpty()) {
                loadNativeAd()
            }
        }
        binding.rcyHome.adapter = adapter
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            imgSearch.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("destination",R.id.homeFragment)
                findNavController().navigate(R.id.searchFragment,bundle)
            }

            imgHeart.setOnSingleClickListener {
                findNavController().navigate(R.id.favouriteFragment)
            }
            rcyHome.scrollToPosition(0)
        }
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().Native_home) {
            Timber.d("loadNativeAd()")
            BkPlusNativeAd.loadNativeAd(
                this,
                BuildConfig.Native_home,
                R.layout.native_onboarding,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        adapter?.populateNativeAd(nativeAd, this@HomeFragment)
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

    private fun loadAdReward() {
        if (BasePrefers.getPrefsInstance().reward_gif) {
            activity?.let {
                if (!adsContainer.isRewardedAdReady(BuildConfig.reward_gif)) {
                    BkPlusAdmob.loadAdRewarded(it, BuildConfig.reward_gif,
                        object : BkPlusAdmobRewardedCallback() {
                            override fun onRewardAdLoaded(rewardedAd: RewardedAd) {
                                super.onRewardAdLoaded(rewardedAd)
                                adsContainer.saveRewardedAd(BuildConfig.reward_gif, rewardedAd)
                            }
                        })
                }

            }
        }
    }

    private fun showRewardAd(action: () -> Unit) {
        if (BasePrefers.getPrefsInstance().reward_gif) {
            activity?.let {
                BkPlusAdmob.showAdRewarded(it, adsContainer.getRewardedAd(BuildConfig.reward_gif),
                    object : BkPlusAdmobRewardedCallback() {
                        override fun onShowAdRequestProgress(tag: String, message: String) {
                            super.onShowAdRequestProgress(tag, message)
                            action.invoke()
                        }

                        override fun onAdFailed(tag: String, errorMessage: String) {
                            super.onAdFailed(tag, errorMessage)
                            adsContainer.removeRewardedAd(BuildConfig.reward_gif)
                        }

                        override fun onAdDismissed(tag: String, message: String) {
                            super.onAdDismissed(tag, message)
                            adsContainer.removeRewardedAd(BuildConfig.reward_gif)
                        }
                    })

            }
        } else {
            action.invoke()
        }
    }

    private fun gotoViewLike(item: Item, listData: ArrayList<Item>) {
        val wallpaper =
            WallPaper(id = item.id, url = item.url, likeCount = item.loves, free = item.free)
        val listItem = listData.map { dataItem ->
            WallPaper(
                id = dataItem.id,
                url = dataItem.url,
                likeCount = dataItem.loves,
                free = dataItem.free
            )
        }.toTypedArray()
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToViewLikeContainerFragment(
                R.id.homeFragment,
                wallpaper,
                listItem
            )
        )
    }

    private fun setupShowForceUpdate() {
        when (compareVersions(BasePrefers.getPrefsInstance().app_version_latest.toString())) {
            0, -1 -> return
        }
        when (compareVersions(BasePrefers.getPrefsInstance().app_version_force_update.toString())) {
            1 -> {
                val dialog = ForceUpdateDialog()
                dialog.isCancelable = false
                dialog.show(childFragmentManager, null)
            }

            0, -1 -> {
                if (isShowDialogForceUpdate == true) {
                    OptionUpdateDialog().show(childFragmentManager, null)
                    isShowDialogForceUpdate = false
                }
            }
        }
    }

    private fun compareVersions(versionA: String): Int {
        val versionTokensA = versionA.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val versionTokensB =
            BuildConfig.VERSION_NAME.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val versionNumbersA: MutableList<Int> = ArrayList()
        val versionNumbersB: MutableList<Int> = ArrayList()
        for (versionToken in versionTokensA) {
            versionNumbersA.add(versionToken.toInt())
        }
        for (versionToken in versionTokensB) {
            versionNumbersB.add(versionToken.toInt())
        }
        val versionASize = versionNumbersA.size
        val versionBSize = versionNumbersB.size
        val maxSize = max(versionASize, versionBSize)
        for (i in 0 until maxSize) {
            if ((if (i < versionASize) versionNumbersA[i] else 0) > (if (i < versionBSize) versionNumbersB[i] else 0)) {
                return 1
            } else if ((if (i < versionASize) versionNumbersA[i] else 0) < (if (i < versionBSize) versionNumbersB[i] else 0)) {
                return -1
            }
        }
        return 0
    }
}
