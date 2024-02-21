package com.bkplus.callscreen.ui.main.category.adapter

import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemCategoryBinding

class CategoryAdapter(val onClick: (String) -> Unit) :
    BaseRecyclerViewAdapter<Category, ItemCategoryBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_category
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemCategoryBinding, Category>,
        position: Int
    ) {
        holder.binding.name.text = items[position].name
        holder.binding.number.text = items[position].number.toString() + " wallpapers"
        holder.binding.image.loadImage(items[position].thumbnail)
        holder.binding.image.setOnClickListener {
            items[position].id?.let { it1 -> onClick(it1) }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

