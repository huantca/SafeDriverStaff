package com.bkplus.callscreen.ui.main.favourite

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.viewlike.WallPaper
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
        adapter?.itemAction = { item, listData ->
            val wallpaper = WallPaper(id = item.id?.toInt(), url = item.imageUrl, likeCount = item.loves, free = item.free)
            val listItem = listData.map { dataItem ->
                WallPaper(id = dataItem.id?.toInt(), url = dataItem.imageUrl, likeCount = dataItem.loves, free = wallpaper.free)
            }.toTypedArray()
            findNavController().navigate(
                FavouriteFragmentDirections.actionFavouriteFragmentToViewLikeContainerFragment(
                    wallpaper,
                    listItem
                ))
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