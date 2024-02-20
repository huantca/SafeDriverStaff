package com.bkplus.callscreen.ui.main.category.adapter

import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.loadImage
import com.bkplus.callscreen.ultis.visible
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemCategorySmallBinding

class CategorySmallAdapter(val onClick: (String) -> Unit) :
    BaseRecyclerViewAdapter<Category, ItemCategorySmallBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_category_small
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemCategorySmallBinding, Category>,
        position: Int
    ) {
        holder.binding.name.text = items[position].name
        holder.binding.image.loadImage(items[position].thumbnail)
        holder.binding.image.setOnClickListener {
            items[position].name?.let { it1 -> onClick(it1) }
        }
        if (items[position].selected) holder.binding.outline.visible()
        else holder.binding.outline.gone()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

