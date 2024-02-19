package com.bkplus.callscreen.ui.main.home.search.adapter

import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchBinding

class SearchAdapter :BaseRecyclerViewAdapter<SearchItem, ItemSearchBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchBinding, SearchItem>,
        position: Int
    ) {

    }

}

data class SearchItem (
    val url: String
)
