package com.bkplus.callscreen.ui.main.home.favourite

import androidx.core.view.isVisible
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemLatestBinding

class FavouriteAdapter : BaseRecyclerViewAdapter<WallpaperEntity, LayoutItemLatestBinding>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_latest
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemLatestBinding, WallpaperEntity>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackground)
            imgReward.isVisible = item.free != true
            tvHeart.text = item..toString()
        }
    }



}