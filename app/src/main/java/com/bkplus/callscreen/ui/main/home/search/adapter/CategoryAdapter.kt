package com.bkplus.callscreen.ui.main.home.search.adapter

import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemSearchCategoryBinding

class CategoryAdapter(val onClick: (String) -> Unit) :
    BaseRecyclerViewAdapter<Category, ItemSearchCategoryBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_search_category
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemSearchCategoryBinding, Category>,
        position: Int
    ) {
        holder.binding.name.text = items[position].name
        holder.binding.number.text = items[position].number.toString() + " wallpapers"
        holder.binding.image.loadImage(items[position].thumbnail)
        holder.binding.image.setOnClickListener {
            items[position].name?.let { it1 -> onClick(it1) }
        }
    }

    override fun getItemCount(): Int {
        return if (items.size > 4) 4 else items.size
    }
}
