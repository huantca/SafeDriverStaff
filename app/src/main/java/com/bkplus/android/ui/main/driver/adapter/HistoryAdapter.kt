package com.bkplus.android.ui.main.driver.adapter

import androidx.core.view.isVisible
import com.bkplus.android.model.Trip
import com.bkplus.android.ultis.convertLongToTime
import com.bkplus.android.ultis.numberToVND
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemRcyHistoryUserBinding

class HistoryAdapter : BaseRecyclerViewAdapter<Trip, ItemRcyHistoryUserBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_rcy_history_user
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemRcyHistoryUserBinding, Trip>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            tvDate.text = item.date_of_hire?.let { convertLongToTime(it) }
            tvFee.text = item.fee?.let { numberToVND(it) }
            tvStatus.text = item.status.toString()
            tvStartLocation.text = item.pick_up_location
            tvEndLocation.text = item.drop_off_location
            tvNote.isVisible = item.note != null
            tvNote.text = item.note
        }
    }
}