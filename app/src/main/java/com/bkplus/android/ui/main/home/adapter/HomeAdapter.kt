package com.bkplus.android.ui.main.home.adapter

import com.bkplus.android.model.Trip
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LayoutItemLatestBinding

class HomeAdapter: BaseRecyclerViewAdapter<Trip,LayoutItemLatestBinding>() {

    var action : ((trip: Trip) -> Unit )?= null
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_latest
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemLatestBinding, Trip>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            tvHeart.text = item.user?.name
            btnAccept.setOnSingleClickListener{
                action?.invoke(item)
            }
        }
    }
}