package com.bkplus.callscreen.ui.main.history

import com.bkplus.callscreen.database.WallpaperEntity
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.loadImage
import com.bkplus.callscreen.ultis.visible
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemHistoryBinding

class HistoryRecyclerViewAdapter(
    val onSelectedAll: () -> Unit = {},
    val onNotSelectedAll: () -> Unit = {},
    val onClicked: () -> Unit = {},
    val onNavigate : (item: WallpaperEntity) -> Unit = {}
) : BaseRecyclerViewAdapter<WallpaperEntity, ItemHistoryBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_history
    }

    var isSelecting = false
    var selectedCount = 0
        get() = items.count { it.isSelected }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemHistoryBinding, WallpaperEntity>, position: Int
    ) {
        holder.binding.image.loadImage(items[position].imageUrl)
        val checkSelecting = {
            holder.binding.apply {
                if (isSelecting && items[position].isSelected) {
                    checkBox.visible()
                    checkBoxBlank.gone()
                }
                if (isSelecting && !items[position].isSelected) {
                    checkBoxBlank.visible()
                    checkBox.gone()
                }
                if (!isSelecting) {
                    checkBox.gone()
                    checkBoxBlank.gone()
                }
            }
        }
        checkSelecting()
        holder.binding.image.setOnClickListener {
            items[position].isSelected = items[position].isSelected.not()
            if (items[position].isSelected) selectedCount++
            else selectedCount--
            if (selectedCount == itemCount) onSelectedAll()
            else onNotSelectedAll()
            if (isSelecting) {
                onClicked()
            } else {
                onNavigate.invoke(items[position])
            }
            checkSelecting()
        }
    }
}
