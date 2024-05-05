package com.bkplus.android.ui.main.driver.adapter

import android.annotation.SuppressLint
import com.bkplus.android.model.Trip
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.numberToVND
import com.bkplus.android.ultis.setOnSingleClickListener
import com.bkplus.android.ultis.visible
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemRcyHomeDriverBinding

class DriverAdapter : BaseRecyclerViewAdapter<Trip, ItemRcyHomeDriverBinding>() {

    var action: ((trip: Trip) -> Unit)? = null
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_rcy_home_driver
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemRcyHomeDriverBinding, Trip>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            tvName.text = item.user?.name
            tvCost.text = item.fee?.let { numberToVND(it) }
            tvNameCar.text = item.car_name
            tvRangeCar.text = item.range_of_vehicle
            tvKm.text = String.format("%.2f Km", item.km)
            tvStartLocation.text = item.pick_up_location
            tvEndLocation.text = item.drop_off_location
            tvNote.text = item.note
            if (item.hourly_rental != 0){
                tvRentFor.visible()
                tvRentFor.text = root.context.getString(R.string.rent_for) + item.hourly_rental + "h"
            }else{
                tvRentFor.gone()
            }

            btnAccept.setOnSingleClickListener {
                action?.invoke(item)
            }
        }
    }
}