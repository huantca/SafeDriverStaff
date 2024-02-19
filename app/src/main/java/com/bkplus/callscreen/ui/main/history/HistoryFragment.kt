package com.bkplus.callscreen.ui.main.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.deleteFileIfExist
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
        viewModel.getData()
        binding.isSelecting = false
        binding.isSelectedAll = false
        adapter = HistoryRecyclerViewAdapter({
            binding.isSelectedAll = true
        }, {
            binding.isSelectedAll = false
        }, {
            updateDeleteText()
        })
        binding.recyclerView.adapter = adapter
        viewModel.list.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.updateItems(ArrayList(it))
            }
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
        }

        binding.textSelectAll.setOnClickListener {
            adapter.items.forEach { it.isSelected = true }
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
}
