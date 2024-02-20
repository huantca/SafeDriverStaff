package com.bkplus.callscreen.ui.main.category

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.category.adapter.CategorySmallAdapter
import com.bkplus.callscreen.ui.main.category.adapter.DetailAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ultis.visible
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCategoryDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

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

    override fun setupData() {
        viewModel.getSearch()
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
            var chosen = categoryList?.firstOrNull { it.name == arguments?.getString("category") }
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
            categoryAdapter.items.firstOrNull { it.selected }?.selected = false
            categoryAdapter.items.firstOrNull { it.name == category }?.selected = true
            detailAdapter.updateItems(searchList)
            categoryAdapter.notifyDataSetChanged()
            binding.recyclerViewCategory.scrollToPosition(categoryAdapter.items.indexOfFirst { it.name == category })
            binding.titleTop.text = category
            binding.titleTop.visible()
        }
    }
}
