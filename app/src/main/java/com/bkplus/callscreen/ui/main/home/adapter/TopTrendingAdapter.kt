package com.bkplus.callscreen.ui.main.home.adapter

import androidx.core.view.isVisible
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemTopTrendingBinding


class TopTrendingAdapter : BaseRecyclerViewAdapter<Item, LayoutItemTopTrendingBinding>() {
    var onItemClick: (Item) -> Unit? = {}
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_top_trending
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemTopTrendingBinding, Item>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            BasePrefers.getPrefsInstance().listItemsFree.forEach {
                if (it.url == item.url) {
                    item.free = true
                    return@forEach
                }
            }
            if (!BasePrefers.getPrefsInstance().reward_gif) {
                imgReward.gone()
                item.free = true
            }else{
                imgReward.isVisible = item.free != true
            }
            root.setOnSingleClickListener {
                onItemClick(item)
            }
            Glide.with(root.context)
                .load(item.thumbnail)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackground)
            tvHeart.text = item.loves.toString()
        }
    }

}
