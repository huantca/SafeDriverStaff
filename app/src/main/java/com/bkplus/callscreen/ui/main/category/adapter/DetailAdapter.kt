package com.bkplus.callscreen.ui.main.category.adapter

import androidx.core.view.isVisible
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemCategoryDetailBinding
import com.harrison.myapplication.databinding.ItemSearchBinding

class DetailAdapter :BaseRecyclerViewAdapter<Item, ItemCategoryDetailBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_category_detail
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemCategoryDetailBinding, Item>,
        position: Int
    ) {
        holder.binding.image.loadImage(items[position].url)
        holder.binding.tvHeart.text = items[position].loves.toString()
        holder.binding.icReward.isVisible = items[position].free != true
    }
}
