package com.bkplus.callscreen.ui.main.home.adapter

import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bkplus.callscreen.ultis.toArrayList
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutHomeLatestBinding
import com.harrison.myapplication.databinding.LayoutHomeTrendyBinding
import com.harrison.myapplication.databinding.LayoutRcyHomeBinding

class HomeAdapter : BaseRecyclerViewAdapter<HomeSectionEntity, LayoutRcyHomeBinding>() {

    var viewAll: (arrayList: ArrayList<Item>) -> Unit? = {}
    var onItemRcvClick: (Item, listData: ArrayList<Item>) -> Unit? =
        { item: Item, listData: ArrayList<Item> -> }

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
        val listViewAll = ArrayList<Item>()
        if (homeSectionEntity.id == 1) {
            val shuffled = homeSectionEntity.items?.shuffled()
            val randomList = shuffled?.subList(0,15)
            randomList?.forEach {
                it?.let { listTrendy.add(it) }
            }
            homeSectionEntity.items?.forEach {
                it?.let { listViewAll.add(it) }
            }
        }
        topTrendyAdapter.updateItems(listTrendy)
        binding.rcyTopTrendy.adapter = topTrendyAdapter
        topTrendyAdapter.onItemClick = { onItemRcvClick(it, listTrendy) }
        binding.tvViewAll.setOnClickListener {
            viewAll.invoke(listViewAll)
        }
    }

    private fun bindItemLatest(
        homeSectionEntity: HomeSectionEntity,
        binding: LayoutHomeLatestBinding
    ) {
        val latestAdapter = LatestAdapter()
        val listLatest = ArrayList<Latest>()
        if (homeSectionEntity.id == 2) {
            val shuffled = homeSectionEntity.items?.shuffled()
            val randomList = shuffled?.subList(0,15)
            randomList?.forEachIndexed { index, item ->
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
        latestAdapter.itemAction = {
            homeSectionEntity.items?.let { it1 ->
                onItemRcvClick(it, it1.toArrayList())
            }
        }
    }

}