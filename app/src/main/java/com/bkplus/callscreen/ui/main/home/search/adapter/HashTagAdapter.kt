package com.bkplus.callscreen.ui.main.home.search.adapter

import com.bkplus.callscreen.api.entity.Category
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchHashtagBinding

class HashTagAdapter(val onClick: (String) -> Unit) :
    BaseRecyclerViewAdapter<Category, ItemSearchHashtagBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_hashtag
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchHashtagBinding, Category>,
        position: Int
    ) {
        holder.binding.name.text = "#" + items[position].name
        holder.binding.name.setOnClickListener {
            items[position].name?.let { it1 -> onClick(it1) }
        }
    }
}
