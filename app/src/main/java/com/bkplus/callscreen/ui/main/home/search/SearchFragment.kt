package com.bkplus.callscreen.ui.main.home.search

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callback.BkPlusAdmobRewardedCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.home.search.adapter.CategoryAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.HashTagAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.SearchAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.TrendingAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ui.widget.RewardDialog
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_search

    @Inject
    lateinit var adsContainer: AdsContainer
    private val viewModel: HomeViewModel by viewModels()

    private val categoryAdapter = CategoryAdapter {
        binding.editTextId.setText(it)
        binding.editTextId.setSelection(binding.editTextId.length())
    }
    private val hashtagAdapter = HashTagAdapter {
        binding.editTextId.setText(it)
        binding.editTextId.setSelection(binding.editTextId.length())
    }
    private val trendingAdapter = TrendingAdapter()
    private val searchAdapter = SearchAdapter()

    private var categoryList: ArrayList<Category>? = null
    private var homeSection: ArrayList<HomeSectionEntity>? = null

    override fun setupData() {
        viewModel.getSearch()
        loadAdReward()
        loadInterBackHome()
        binding.apply {
            recyclerViewCategory.adapter = categoryAdapter
            recyclerViewHashtag.adapter = hashtagAdapter
            recyclerViewTrending.adapter = trendingAdapter
            recyclerSearch.adapter = searchAdapter
        }
    }

    override fun setupListener() {
        binding.apply {
            icBack.setOnClickListener {
                if (recyclerSearch.isVisible) {
                    recyclerSearch.gone()
                    layoutBottom.visible()
                } else {
                    showInterBackHome {
                        findNavController().popBackStack()
                    }
                }
            }

            editTextId.addTextChangedListener {
                if (editTextId.text.isNotBlank()) {
                    recyclerSearch.visible()
                    layoutBottom.gone()
                } else {
                    recyclerSearch.gone()
                    layoutBottom.visible()
                }
                findImagesByCategory(it.toString())
            }
        }
        searchAdapter.onItemRcvClick = { item, listData ->
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
        trendingAdapter.onItemRcvClick = { item, listData ->
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
    }

    override fun setupUI() {
        viewModel.homeSectionAndCategoryLiveData.observe(viewLifecycleOwner) { boolean ->
            categoryList = viewModel.categories.value
            homeSection = viewModel.homeSectionLiveData.value
            categoryList?.let {
                hashtagAdapter.updateItems(it)
            }
            homeSection?.firstOrNull { it.name == "Top Trending" }?.let { section ->
                section.items?.let { items ->
                    trendingAdapter.updateItems(ArrayList(items))
                }
            }
            categoryList?.let { categories ->
                categories.sortByDescending { it.number }
                categoryAdapter.updateItems(categories)
            }
        }
    }

    private fun findImagesByCategory(category: String) {
        lifecycleScope.launch {
            val searchList = ArrayList<Item>()
            homeSection?.forEach { section ->
                section.items?.filter {
                    it.category?.lowercase(Locale.ROOT)
                        ?.contains(category.lowercase(Locale.ROOT)) == true
                }?.forEach { item ->
                    item.let {
                        searchList.add(it)
                    }
                }
            }
            searchAdapter.updateItems(searchList)
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

    private fun gotoViewLike(item: Item, listData: ArrayList<Item>) {
        val item1 =
            WallPaper(id = item.id, url = item.url, free = item.free, likeCount = item.loves)
        val listItem = listData.map { item ->
            WallPaper(id = item.id, url = item.url, free = item.free, likeCount = item.loves)
        }.toTypedArray()
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToViewLikeContainerFragment(
                R.id.homeFragment,
                item1,
                listItem
            )
        )
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
}
