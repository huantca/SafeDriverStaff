package com.bkplus.callscreen.ui.main.home.search.adapter

import androidx.core.view.isVisible
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchBinding

class SearchAdapter :BaseRecyclerViewAdapter<Item, ItemSearchBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchBinding, Item>,
        position: Int
    ) {
        holder.binding.image.loadImage(items[position].url)
        holder.binding.icReward.isVisible = items[position].free != true
    }
}
