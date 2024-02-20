package com.bkplus.callscreen.ui.main.category

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.category.adapter.CategoryAdapter
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_category

    private val viewModel: HomeViewModel by viewModels()

    private val categoryAdapter = CategoryAdapter {
        findNavController().navigate(R.id.categoryDetailFragment, Bundle().apply {
            putString("category", it)
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
        viewModel.getSearch()
        binding.recyclerViewCategory.adapter = categoryAdapter
    }

    override fun setupUI() {
        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.updateItems(it)
        }
    }

    override fun setupListener() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }
}
