package com.bkplus.callscreen.ui.main.home.favourite

import androidx.core.view.isVisible
import com.bkplus.callscreen.database.WallpaperEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemFavouriteBinding

class FavouriteAdapter : BaseRecyclerViewAdapter<WallpaperEntity, LayoutItemFavouriteBinding>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_favourite
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemFavouriteBinding, WallpaperEntity>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackground)
            imgReward.isVisible = item.free != true
            tvHeart.text = item.loves.toString()
        }
    }
}