package com.bkplus.callscreen.ui.main.home.search.adapter

import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchHashtagBinding

class HashTagAdapter : BaseRecyclerViewAdapter<HashTag, ItemSearchHashtagBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_hashtag
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchHashtagBinding, HashTag>,
        position: Int
    ) {

    }

}

data class HashTag(
    val name: String
)
