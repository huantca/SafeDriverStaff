package com.bkplus.callscreen.ui.main.home.favourite

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentFavouriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment: BaseFragment<FragmentFavouriteBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_favourite

    private var adapter: FavouriteAdapter? = null
    private val viewModel: FavouriteViewModel by viewModels()

    override fun setupData() {
        super.setupData()
        viewModel.getData()
        adapter = FavouriteAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.favouriteList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.updateItems(ArrayList(it))
            }
        }
    }

    override fun setupUI() {
        super.setupUI()

    }

    override fun setupListener() {
        super.setupListener()

        binding.apply {
            backBtn.setOnSingleClickListener {
                findNavController().popBackStack()
            }
        }
    }
}