package com.bkplus.callscreen.ui.main.home.search.adapter

import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchCategoryBinding

class CategoryAdapter : BaseRecyclerViewAdapter<Category, ItemSearchCategoryBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_category
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchCategoryBinding, Category>,
        position: Int
    ) {

    }

}

data class Category(
    val url: String,
    val name: String,
    val number: Int,
)
