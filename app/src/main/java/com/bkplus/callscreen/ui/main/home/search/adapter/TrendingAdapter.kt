package com.bkplus.callscreen.ui.main.home.search.adapter

import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.loadImage
import com.bkplus.callscreen.ultis.visible
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchTrendingBinding

class TrendingAdapter : BaseRecyclerViewAdapter<Item?, ItemSearchTrendingBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_trending
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchTrendingBinding, Item?>,
        position: Int
    ) {
        holder.binding.image.loadImage(items[position]?.url)
        when (items[position]?.free) {
            false -> holder.binding.icReward.visible()
            else -> holder.binding.icReward.gone()
        }
    }
}
