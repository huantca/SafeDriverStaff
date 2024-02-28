package com.bkplus.callscreen.ui.main.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.bkplus.callscreen.ads.EventTracking
import com.bkplus.callscreen.ads.TrackingManager
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.database.WallpaperEntity
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ultis.deleteFileIfExist
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.toArrayList
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    private val viewModel: HistoryViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_history

    private lateinit var adapter: HistoryRecyclerViewAdapter

    companion object {
        fun newInstance(): HistoryFragment {
            val args = Bundle()
            val fragment = HistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupData() {
        TrackingManager.tracking(EventTracking.fb003_history_view)
        viewModel.getData()
        binding.isSelecting = false
        binding.isSelectedAll = false
        adapter = HistoryRecyclerViewAdapter({
            binding.isSelectedAll = true
        }, {
            binding.isSelectedAll = false
        }, {
            updateDeleteText()
        }, { item, list ->
            gotoViewLike(item,list)
        })
        binding.recyclerView.adapter = adapter
        viewModel.list.observe(viewLifecycleOwner) {
            adapter.updateItems(it.toArrayList())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupListener() {
        binding.selectAll.setOnClickListener {
            adapter.isSelecting = !adapter.isSelecting
            binding.isSelecting = adapter.isSelecting
            adapter.notifyDataSetChanged()
        }

        binding.deleteButton.setOnClickListener {
            adapter.items.forEach {
                if (it.isSelected) viewModel.delete(it)
                it.imageUri?.deleteFileIfExist()
            }
            adapter.items.removeIf { it.isSelected }
            binding.selectAll.performClick()
            adapter.notifyDataSetChanged()
            if (adapter.itemCount == 1) {
                binding.selectAll.gone()
            }
        }

        binding.textSelectAll.setOnClickListener {
            adapter.items.forEachIndexed { index, item -> item.isSelected = index > 0 }
            adapter.notifyDataSetChanged()
            binding.isSelectedAll = true
            updateDeleteText()
        }

        binding.textDeSelectAll.setOnClickListener {
            adapter.items.forEach { it.isSelected = false }
            binding.selectAll.performClick()
            binding.isSelectedAll = false
            updateDeleteText()
        }
    }

    private fun updateDeleteText() {
        binding.deleteButton.text = "Delete ${adapter.selectedCount} images"
    }

    private fun gotoViewLike(wallpaper: WallpaperEntity, list: ArrayList<WallpaperEntity>) {
        val item = WallPaper(
            id = wallpaper.id?.toIntOrNull(),
            url = wallpaper.imageUrl,
            free = wallpaper.free,
            likeCount = wallpaper.loves
        )
        val listItem = list.map { item ->
            WallPaper(
                id = item.id?.toIntOrNull(),
                url = item.imageUrl,
                free = item.free,
                likeCount = item.loves
            )
        }.toTypedArray()
        loadAndShowInertAd {
            findNavController().navigate(
                HistoryFragmentDirections.actionHistoryFragmentToViewLikeContainerFragment(
                    R.id.historyFragment,
                    item,
                    listItem
                )
            )
        }
    }

    private fun loadAndShowInertAd(action: () -> Unit) {
        if (BasePrefers.getPrefsInstance().intersitial_viewhistory) {
            activity?.let {
                BkPlusAdmob.showAdInterstitial(it, BuildConfig.intersitial_viewhistory,
                    object : BkPlusAdmobInterstitialCallback() {
                        override fun onShowAdRequestProgress(tag: String, message: String) {
                            super.onShowAdRequestProgress(tag, message)
                            action.invoke()
                        }
                    })
            }
        } else {
            action.invoke()
        }
    }
}
