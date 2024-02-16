package com.bkplus.callscreen.ui.main.home.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bumptech.glide.Glide
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemNativeOnboardBinding
import com.harrison.myapplication.databinding.LayoutItemLatestBinding

class LatestAdapter : BaseRecyclerViewAdapter<Latest,ViewDataBinding>() {

    private var onClickListener: OnClickListener? = null
    private var activity: Activity? = null
    companion object {
        const val ITEM = 0
        const val ADS = 1
    }
    override fun getLayoutId(viewType: Int): Int {
        return when(viewType){
            ADS -> R.layout.item_native_onboard
            else -> R.layout.layout_item_latest
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items.getOrNull(position)?.type ?: ITEM
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()
    }

    private fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (items[position].type) {
                    ADS -> 3
                    else -> 1
                }
            }
        }
    }
    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewDataBinding, Latest>,
        position: Int
    ) {
        val item = items[position]
        when(getItemViewType(position)){
            ADS ->{
                (holder.binding as? ItemNativeOnboardBinding)?.let { bindAds(item, it) }
            }

            else ->{
                (holder.binding as? LayoutItemLatestBinding)?.let { bindItem(item, it) }
            }
        }
    }

    private fun bindItem(item: Latest, binding: LayoutItemLatestBinding) {
        binding.apply {
            Glide.with(root.context).load(R.mipmap.sample_image).into(imgBackground)
        }
    }

    private fun bindAds(item: Latest, binding: ItemNativeOnboardBinding) {
        if (item.nativeAd == null) {
            return
        }
    }

    fun bindAds(nativeAd: BkNativeAd?, activity: Activity) {
        this.activity = activity
        items.forEachIndexed { index, item ->
            if (item.type == ADS) {
                item.nativeAd = nativeAd
                notifyItemChanged(index)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun actionConnect(item: Latest)
    }
}