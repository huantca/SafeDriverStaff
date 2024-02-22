package com.bkplus.callscreen.ui.main.category

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
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
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCategoryDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class CategoryDetailFragment : BaseFragment<FragmentCategoryDetailBinding>() {
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
            findNavController().popBackStack()
        }
        detailAdapter.onItemRcvClick = { item, listData ->
            val item = WallPaper(id = item.id, url = item.url, likeCount = item.loves, free = item.free)
            val listItem = listData.map { item ->
                WallPaper(id = item.id, url = item.url, likeCount = item.loves, free = item.free)
            }.toTypedArray()
            findNavController().navigate(
                CategoryDetailFragmentDirections.actionCategoryDetailFragmentToViewLikeContainerFragment(
                    item,
                    listItem
                )
            )
        }
    }

    private fun findImagesByCategory(category: String) {
        lifecycleScope.launch {
            val searchList = ArrayList<Item>()
            homeSection?.forEach { section ->
                section.items?.filter {
                    it.category == category
                }?.forEach { item ->
                    item.let {
                        searchList.add(it)
                    }
                }
            }
            categoryAdapter.items.firstOrNull { it.selected }?.selected = false
            categoryAdapter.items.firstOrNull { it.name == category }?.selected = true
            detailAdapter.updateItems(if (BasePrefers.getPrefsInstance().native_viewcategories) addNativeAd(searchList) else searchList)
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
        if (BasePrefers.getPrefsInstance().native_categories) {
            Timber.d("loadNativeAd()")
            BkPlusNativeAd.loadNativeAd(
                this,
                BuildConfig.Native_toptrending,
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
}
