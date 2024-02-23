package com.bkplus.callscreen.ui.main.favourite

import androidx.core.view.isVisible
import com.bkplus.callscreen.database.FavoriteEntity
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemFavouriteBinding

class FavouriteAdapter : BaseRecyclerViewAdapter<FavoriteEntity, LayoutItemFavouriteBinding>() {

    var itemAction: ((FavoriteEntity, listDat: ArrayList<FavoriteEntity>) -> Unit)? = null

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_favourite
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemFavouriteBinding, FavoriteEntity>,
        position: Int
    ) {
        val item = items.getOrNull(position) ?: return
        holder.binding.apply {
            Glide.with(root.context)
                .load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackground)
            imgReward.isVisible = item.free != true
            tvHeart.text = item.loves.toString()
            root.setOnSingleClickListener {
                itemAction?.invoke(
                    item, items
                )
            }
        }
    }
}