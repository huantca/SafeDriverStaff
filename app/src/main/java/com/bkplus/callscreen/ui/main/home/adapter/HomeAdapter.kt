package com.bkplus.callscreen.ui.main.home.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bkplus.callscreen.ultis.toArrayList
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutHomeLatestBinding
import com.harrison.myapplication.databinding.LayoutHomeTrendyBinding
import com.harrison.myapplication.databinding.LayoutRcyHomeBinding

class HomeAdapter : BaseRecyclerViewAdapter<HomeSectionEntity, ViewDataBinding>() {

    var viewAll: (arrayList: ArrayList<Item>) -> Unit? = {}
    var onItemRcvClick: (Item, listData: ArrayList<Item>) -> Unit? =
        { item: Item, listData: ArrayList<Item> -> }
    private val latestAdapter = LatestAdapter()

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
        holder: BaseViewHolder<ViewDataBinding, HomeSectionEntity>,
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
        val topTrendingAdapter = TopTrendingAdapter()
        val listTrendy = ArrayList<Item>()
        val listViewAll = ArrayList<Item>()
        if (homeSectionEntity.id == 1) {
            val shuffled = homeSectionEntity.items?.shuffled()
            val randomList = shuffled?.subList(0,15)
            randomList?.forEach {
                it.let { listTrendy.add(it) }
            }
            homeSectionEntity.items?.forEach {
                it.let { listViewAll.add(it) }
            }
        }
        topTrendingAdapter.updateItems(listTrendy)
        binding.rcyTopTrending.adapter = topTrendingAdapter
        binding.title.text = homeSectionEntity.name
        topTrendingAdapter.onItemClick = {
            homeSectionEntity.items?.let { list ->
                onItemRcvClick(it, list.toArrayList())
            }
        }
        binding.tvViewAll.setOnClickListener {
            viewAll.invoke(listViewAll)
        }
    }

    private fun bindItemLatest(
        homeSectionEntity: HomeSectionEntity,
        binding: LayoutHomeLatestBinding
    ) {
        val listLatest = ArrayList<Latest>()
        if (homeSectionEntity.id == 2) {
            val shuffled = homeSectionEntity.items?.shuffled()
            val randomList = shuffled?.subList(0,15)
            randomList?.forEachIndexed { index, item ->
                val count = index + 1;
                listLatest.add(
                    Latest(
                        item.url,
                        item.category,
                        item.loves,
                        item.free,
                        null,
                        LatestAdapter.ITEM
                    )
                )
                if (count % 6 == 0) {
                    listLatest.add(Latest(nativeAd = null, type = LatestAdapter.ADS))
                }
            }
        }
        binding.tvLatest.text = homeSectionEntity.name
        latestAdapter.updateItems(listLatest)
        binding.rcyLatest.adapter = latestAdapter
        latestAdapter.itemAction = {
            homeSectionEntity.items?.let { it1 ->
                onItemRcvClick(it, it1.toArrayList())
            }
        }
    }

    fun populateNativeAd(nativeAd: BkNativeAd?, fragment: Fragment) {
        latestAdapter.populateNativeAd(nativeAd, fragment)
    }

    fun removeNativeAd() {
        latestAdapter.removeNativeAd()
    }
}
