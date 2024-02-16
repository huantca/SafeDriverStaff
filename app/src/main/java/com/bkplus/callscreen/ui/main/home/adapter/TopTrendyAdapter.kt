package com.bkplus.callscreen.ui.main.home.adapter

import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bumptech.glide.Glide
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemTopTrendyBinding

class TopTrendyAdapter : BaseRecyclerViewAdapter<HomeSectionEntity, LayoutItemTopTrendyBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_top_trendy
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemTopTrendyBinding, HomeSectionEntity>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            Glide.with(root.context).load(R.mipmap.sample_image).into(imgBackground)
        }
    }

}