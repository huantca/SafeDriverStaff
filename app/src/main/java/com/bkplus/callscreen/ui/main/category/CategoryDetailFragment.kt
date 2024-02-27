package com.bkplus.callscreen.ui.main.category

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callback.BkPlusAdmobRewardedCallback
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.ads.bkplus_ads.core.toastDebug
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.category.adapter.CategorySmallAdapter
import com.bkplus.callscreen.ui.main.category.adapter.DetailAdapter
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ui.widget.RewardDialog
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCategoryDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class CategoryDetailFragment : BaseFragment<FragmentCategoryDetailBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer
    override val layoutId: Int
        get() = R.layout.fragment_category_detail

    private val viewModel: HomeViewModel by viewModels()
    private val categoryAdapter = CategorySmallAdapter {
        findImagesByCategory(it)
    }
    private val detailAdapter = DetailAdapter()

    private var categoryList: ArrayList<Category>? = null
    private var homeSection: ArrayList<HomeSectionEntity>? = null
    private var isInit = false
    private var destination: Int? = null

    override fun setupData() {
        if (!isInit) {
            viewModel.getSearch()
            isInit = true
        }
        binding.apply {
            recyclerViewCategory.adapter = categoryAdapter
            recyclerDetail.adapter = detailAdapter
        }
    }

    override fun setupUI() {
        loadAdReward()
        loadInterBackHome()
        destination = arguments?.getInt("destination")
        viewModel.homeSectionAndCategoryLiveData.observe(viewLifecycleOwner) { boolean ->
            categoryList = viewModel.categories.value
            categoryList?.let { categories ->
                categoryAdapter.updateItems(categories)
            }
            homeSection = viewModel.homeSectionLiveData.value
            var chosen = categoryList?.firstOrNull { it.id == arguments?.getString("id") }
            if (chosen == null) chosen = categoryList?.firstOrNull()
            chosen?.let {
                chosen.selected = true
                chosen.name?.let { findImagesByCategory(it) }
            }
        }
    }

    override fun setupListener() {
        binding.icBack.setOnClickListener {
            if (findNavController().previousBackStackEntry?.destination?.id == R.id.searchFragment) {
                findNavController().popBackStack()
            } else {
                showInterBackHome {
                    findNavController().popBackStack()
                }
            }
        }
        detailAdapter.onItemRcvClick = { item, listData ->
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
                }.show(childFragmentManager, "")
            }
        }
    }

    private fun findImagesByCategory(category: String) {
        lifecycleScope.launch {
            val searchList = ArrayList<Item>()
            homeSection?.getOrNull(1)?.items?.filter {
                it.category == category
            }?.forEach { item ->
                item.let {
                    searchList.add(it)
                }
            }
            categoryAdapter.items.firstOrNull { it.selected }?.selected = false
            categoryAdapter.items.firstOrNull { it.name == category }?.selected = true
            detailAdapter.updateItems(
                if (BasePrefers.getPrefsInstance().native_viewcategories) addNativeAd(
                    searchList
                ) else searchList
            )
            if (searchList.isNotEmpty()) {
                loadNativeAd()
            }
            categoryAdapter.notifyDataSetChanged()
            binding.recyclerViewCategory.scrollToPosition(categoryAdapter.items.indexOfFirst { it.name == category })
            binding.titleTop.text = category
            binding.titleTop.visible()
        }
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_viewcategories) {
            Timber.d("loadNativeAd()")
            BkPlusNativeAd.loadNativeAd(
                this,
                BuildConfig.native_viewcategories,
                R.layout.native_onboarding,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        detailAdapter.populateNativeAd(nativeAd, this@CategoryDetailFragment)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        detailAdapter.removeNativeAd()
                    }
                }
            )
        } else {
            detailAdapter.removeNativeAd()
        }
    }

    private fun addNativeAd(items: List<Item>): ArrayList<Item> {
        val result = arrayListOf<Item>()
        items.forEachIndexed { index, item ->
            result.add(item)
            if ((index + 1) % 6 == 0) {
                result.add(Item(type = LatestAdapter.ADS))
            }
        }
        return result
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
                                toastDebug(context, "load reward")
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
        val item1 =
            WallPaper(id = item.id, url = item.url, free = item.free, likeCount = item.loves)
        val listItem = listData.map { it ->
            WallPaper(id = it.id, url = it.url, free = it.free, likeCount = it.loves)
        }.toTypedArray()
        findNavController().navigate(
            CategoryDetailFragmentDirections.actionCategoryDetailFragmentToViewLikeContainerFragment(
                destination ?: R.id.categoryFragment,
                item1,
                listItem
            )
        )
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
