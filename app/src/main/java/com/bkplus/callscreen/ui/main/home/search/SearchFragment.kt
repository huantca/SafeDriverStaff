package com.bkplus.callscreen.ui.main.home.search

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.search.adapter.CategoryAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.HashTagAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.SearchAdapter
import com.bkplus.callscreen.ui.main.home.search.adapter.TrendingAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.visible
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_search

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
                    findNavController().popBackStack()
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
            homeSection?.forEach { section ->
                section.items?.forEach { item ->
                    item?.category?.let { category ->
                        categoryList?.apply {
                            val found = firstOrNull { it.name == category }
                            found?.let {
                                it.number++
                            }
                        }
                    }
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
                    it?.category?.lowercase(Locale.ROOT)
                        ?.contains(category.lowercase(Locale.ROOT)) == true
                }?.forEach { item ->
                    item?.let {
                        searchList.add(it)
                    }
                }
            }
            searchAdapter.updateItems(searchList)
        }
    }
}
