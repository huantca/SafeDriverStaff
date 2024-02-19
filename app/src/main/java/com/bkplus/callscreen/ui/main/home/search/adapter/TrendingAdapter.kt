package com.bkplus.callscreen.ui.main.home.search.adapter

import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchTrendingBinding

class TrendingAdapter : BaseRecyclerViewAdapter<Trending, ItemSearchTrendingBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_trending
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchTrendingBinding, Trending>,
        position: Int
    ) {

    }

}

data class Trending(
    val url: String,
    val heart: Int,
    val isReward: Boolean,
)
