package com.bkplus.callscreen.ui.main.category

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.category.adapter.CategoryAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_category

    private val viewModel: HomeViewModel by activityViewModels()

    private val categoryAdapter = CategoryAdapter {
        findNavController().navigate(R.id.categoryDetailFragment, Bundle().apply {
            putString("id", it)
        })
    }

    companion object {
        fun newInstance(): CategoryFragment {
            val args = Bundle()
            val fragment = CategoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupData() {
        binding.recyclerViewCategory.adapter = categoryAdapter
    }

    override fun setupUI() {
        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.updateItems(if (BasePrefers.getPrefsInstance().native_categories) viewModel.addNativeAd(it) else it)
            if (it.isNotEmpty()) {
                loadNativeAd()
            }
        }
    }

    override fun setupListener() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_categories) {
            Timber.d("loadNativeAd()")
            BkPlusNativeAd.loadNativeAd(
                this,
                BuildConfig.native_categories,
                R.layout.native_onboarding,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        categoryAdapter.populateNativeAd(nativeAd, this@CategoryFragment)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        categoryAdapter.removeNativeAd()
                    }
                }
            )
        } else {
            categoryAdapter.removeNativeAd()
        }
    }
}
