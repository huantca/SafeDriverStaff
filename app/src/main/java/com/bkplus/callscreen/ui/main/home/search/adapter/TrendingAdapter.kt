package com.bkplus.callscreen.ui.main.home.search.adapter

import androidx.core.view.isVisible
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.loadImage
import com.bkplus.callscreen.ultis.visible
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchTrendingBinding

class TrendingAdapter : BaseRecyclerViewAdapter<Item, ItemSearchTrendingBinding>() {

    var onItemRcvClick: (Item, listData: ArrayList<Item>) -> Unit? =
        { item: Item, listData: ArrayList<Item> -> }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_trending
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchTrendingBinding, Item>,
        position: Int
    ) {
        holder.binding.image.loadImage(items[position].url)
        holder.binding.icReward.isVisible = items[position].free != true
        holder.binding.image.setOnClickListener {
            onItemRcvClick.invoke(items[position], items)
        }
    }
}
