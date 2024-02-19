package com.bkplus.callscreen.ui.main.home.adapter

import androidx.core.view.isVisible
import com.bkplus.callscreen.api.entity.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemTopTrendyBinding


class TopTrendyAdapter : BaseRecyclerViewAdapter<Item, LayoutItemTopTrendyBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_top_trendy
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemTopTrendyBinding, Item>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            Glide.with(root.context)
                .load(item.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgBackground)
            imgReward.isVisible = item.free != true
            tvHeart.text = item.loves.toString()
        }
    }

}