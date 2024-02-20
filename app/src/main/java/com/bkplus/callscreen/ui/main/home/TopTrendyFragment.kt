package com.bkplus.callscreen.ui.main.home

import androidx.navigation.fragment.findNavController
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bkplus.callscreen.ui.viewlike.ViewLikeContainerFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseFullScreenDialogFragment
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentTopTrendyBinding


class TopTrendyFragment : BaseFullScreenDialogFragment<FragmentTopTrendyBinding>(), LatestAdapter.OnClickListener {
    override val layoutId: Int
        get() = R.layout.fragment_top_trendy

    private var adapter: LatestAdapter? = null
    private var data: ArrayList<com.bkplus.callscreen.api.entity.Item>? = null
    var dismissDialog : () -> Unit? = {}
    fun setData(data: ArrayList<com.bkplus.callscreen.api.entity.Item>){
        this.data = data
    }
    override fun setupData() {
        super.setupData()
        adapter = LatestAdapter()
        val arr = ArrayList<Latest>()
        data?.forEachIndexed { index, item ->
            val count = index + 1;
            arr.add(Latest(item.url,item.category,item.loves,item.free,null,LatestAdapter.ITEM))
            if (count % 3 == 0) {
                arr.add(Latest(nativeAd = null, type = LatestAdapter.ADS))
            }
        }
        adapter?.updateItems(arr)
        binding.rcyTopTrendy.adapter = adapter
        adapter?.itemAction = actionItem
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            icBack.setOnSingleClickListener {
                dismiss()
                dismissDialog.invoke()
            }
        }
    }

    override fun actionConnect(item: Latest) {
    }

    private val actionItem: (Latest) -> Unit = { item ->
        findNavController().navigate(R.id.viewLikeContainerFragment)
    }
}