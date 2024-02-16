package com.bkplus.callscreen.ui.main.home.adapter

import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutHomeLatestBinding
import com.harrison.myapplication.databinding.LayoutHomeTrendyBinding
import com.harrison.myapplication.databinding.LayoutRcyHomeBinding

class HomeAdapter : BaseRecyclerViewAdapter<HomeSectionEntity, LayoutRcyHomeBinding>() {

    var viewAll: (arrayList: ArrayList<Item>) -> Unit? = {}
    companion object {
        const val TRENDY = 0
        const val LATEST = 1
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            LATEST -> R.layout.layout_home_latest
            else -> R.layout.layout_home_trendy
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TRENDY
            else -> LATEST
        }
    }
    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutRcyHomeBinding, HomeSectionEntity>,
        position: Int
    ) {
        val item = items[position]
        when (getItemViewType(position)) {
            TRENDY -> {
                (holder.binding as? LayoutHomeTrendyBinding)?.let { bindItemTopTrendy(item, it) }
            }

            else -> {
                (holder.binding as? LayoutHomeLatestBinding)?.let { bindItemLatest(item, it) }
            }
        }

    }

    private fun bindItemTopTrendy(
        homeSectionEntity: HomeSectionEntity,
        binding: LayoutHomeTrendyBinding
    ) {
        val topTrendyAdapter = TopTrendyAdapter()
        val listTrendy = ArrayList<Item>()
        if (homeSectionEntity.id == 1) {
            homeSectionEntity.items?.forEach{  item ->
                item?.let { listTrendy.add(it) }
            }
        }
        topTrendyAdapter.updateItems(listTrendy)
        binding.rcyTopTrendy.adapter = topTrendyAdapter
        binding.tvViewAll.setOnClickListener {
            viewAll.invoke(listTrendy)
        }
    }

    private fun bindItemLatest(
        homeSectionEntity: HomeSectionEntity,
        binding: LayoutHomeLatestBinding
    ) {
        val latestAdapter = LatestAdapter()
        val listLatest = ArrayList<Latest>()
        if (homeSectionEntity.id == 2) {
            homeSectionEntity.items?.forEachIndexed { index, item ->
                val count = index + 1;
                listLatest.add(
                    Latest(
                        item?.url,
                        item?.category,
                        item?.loves,
                        item?.free,
                        null,
                        LatestAdapter.ITEM
                    )
                )
                if (count % 3 == 0) {
                    listLatest.add(Latest(nativeAd = null, type = LatestAdapter.ADS))
                }
            }
        }
        latestAdapter.updateItems(listLatest)
        binding.rcyLatest.adapter = latestAdapter
    }
}