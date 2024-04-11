package com.bkplus.android.ui.main.driver.adapter

import com.bkplus.android.model.Trip
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemRcyHomeDriverBinding

class DriverAdapter : BaseRecyclerViewAdapter<Trip, ItemRcyHomeDriverBinding>() {

    var action: ((trip: Trip) -> Unit)? = null
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_rcy_home_driver
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemRcyHomeDriverBinding, Trip>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            tvName.text = item.user?.name
            tvCost.text = item.fee.toString()
            tvTypeVehicle.text = item.vehicle_type
            tvStartLocation.text = item.pick_up_location
            tvEndLocation.text = item.drop_off_location
            tvNote.text = item.note
            btnAccept.setOnSingleClickListener {
                action?.invoke(item)
            }
        }
    }
}