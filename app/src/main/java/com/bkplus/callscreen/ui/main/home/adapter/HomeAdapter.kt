package com.bkplus.callscreen.ui.main.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutRcyHomeBinding

class HomeAdapter : BaseRecyclerViewAdapter<HomeSectionEntity, LayoutRcyHomeBinding>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_rcy_home
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutRcyHomeBinding, HomeSectionEntity>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            setupDataTopTrendy(rcyTopTrendy)
            setupDataLatest(rcyLatest)
            tvViewAll.setOnSingleClickListener {

            }
        }
    }

    private fun setupDataLatest(rcyLatest: RecyclerView) {
        val latestAdapter = LatestAdapter()
        val arr2 = ArrayList<Latest>()
        arr2.add(Latest(type = LatestAdapter.ITEM))
        arr2.add(Latest(type = LatestAdapter.ITEM))
        arr2.add(Latest(type = LatestAdapter.ITEM))
        arr2.add(Latest(type = LatestAdapter.ADS))
        arr2.add(Latest(type = LatestAdapter.ITEM))
        arr2.add(Latest(type = LatestAdapter.ITEM))
        arr2.add(Latest(type = LatestAdapter.ITEM))
        arr2.add(Latest(type = LatestAdapter.ADS))
        latestAdapter.updateItems(arr2)
        rcyLatest.adapter = latestAdapter
    }

    private fun setupDataTopTrendy(rcyTopTrendy: RecyclerView) {
        val topTrendyAdapter = TopTrendyAdapter()
        val arr1 = ArrayList<HomeSectionEntity>()
        arr1.add(HomeSectionEntity())
        arr1.add(HomeSectionEntity())
        arr1.add(HomeSectionEntity())
        arr1.add(HomeSectionEntity())
        arr1.add(HomeSectionEntity())
        arr1.add(HomeSectionEntity())
        arr1.add(HomeSectionEntity())
        topTrendyAdapter.updateItems(arr1)
        rcyTopTrendy.adapter = topTrendyAdapter
    }
}