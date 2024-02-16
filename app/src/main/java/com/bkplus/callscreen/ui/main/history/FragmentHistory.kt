package com.bkplus.callscreen.ui.main.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.HomeFragment
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.visible
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHistoryBinding


class FragmentHistory : BaseFragment<FragmentHistoryBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_history

    lateinit var adapter: HistoryRecyclerViewAdapter

    companion object {
        fun newInstance(): FragmentHistory {
            val args = Bundle()
            val fragment = FragmentHistory()
            fragment.arguments = args
            return fragment
        }
    }
    val testList = arrayListOf(
        HistoryItem(1, false),
        HistoryItem(2, false),
        HistoryItem(3, false),
        HistoryItem(4, false),
        HistoryItem(5, false),
        HistoryItem(6, false),
        HistoryItem(7, false),
        HistoryItem(8, false),
        HistoryItem(9, false),
        HistoryItem(5, false),
        HistoryItem(6, false),
        HistoryItem(7, false),
        HistoryItem(8, false),
        HistoryItem(9, false),
        HistoryItem(5, false),
        HistoryItem(6, false),
        HistoryItem(7, false),
        HistoryItem(8, false),
        HistoryItem(9, false),
    )

    override fun setupData() {
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
        adapter.updateItems(testList)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupListener() {
        binding.selectAll.setOnClickListener {
            adapter.isSelecting = !adapter.isSelecting
            binding.isSelecting = adapter.isSelecting
            adapter.notifyDataSetChanged()
        }

        binding.deleteButton.setOnClickListener {
            testList.removeIf { it.isSelected }
            binding.selectAll.performClick()
            adapter.notifyDataSetChanged()
        }

        binding.textSelectAll.setOnClickListener {
            testList.forEach { it.isSelected = true }
            adapter.notifyDataSetChanged()
            binding.isSelectedAll = true
            updateDeleteText()
        }

        binding.textDeSelectAll.setOnClickListener {
            testList.forEach { it.isSelected = false }
            binding.selectAll.performClick()
            binding.isSelectedAll = false
            updateDeleteText()
        }
    }

    private fun updateDeleteText() {
        binding.deleteButton.text = "Delete ${adapter.selectedCount} images"
    }
}
