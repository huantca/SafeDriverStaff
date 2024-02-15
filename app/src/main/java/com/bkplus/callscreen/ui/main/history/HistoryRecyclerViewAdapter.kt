package com.bkplus.callscreen.ui.main.history

import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemHistoryBinding

class HistoryRecyclerViewAdapter(
    val onSelectedAll: () -> Unit = {},
    val onNotSelectedAll: () -> Unit = {},
    val onClicked: () -> Unit = {},
) : BaseRecyclerViewAdapter<HistoryItem, ItemHistoryBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_history
    }

    var isSelecting = false
    var selectedCount = 0
        get() = items.count { it.isSelected }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemHistoryBinding, HistoryItem>, position: Int
    ) {
        holder.binding.apply {
            isSelecting = this@HistoryRecyclerViewAdapter.isSelecting
            if (isSelecting == false) isSelected = false
            isSelected = items[position].isSelected
            cardView.setOnClickListener {
                items[position].isSelected = items[position].isSelected.not()
                isSelected = items[position].isSelected
                if (isSelected == true) selectedCount++
                else selectedCount--
                if (selectedCount == itemCount) onSelectedAll()
                else onNotSelectedAll()
                onClicked()
            }
        }
    }
}

data class HistoryItem(
    val id: Int,
    var isSelected: Boolean,
)
