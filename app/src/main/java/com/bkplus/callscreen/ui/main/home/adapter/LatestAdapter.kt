package com.bkplus.callscreen.ui.main.home.adapter

import android.app.Activity
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bkplus.callscreen.ultis.loadImage
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemNativeHomeBinding
import com.harrison.myapplication.databinding.LayoutItemLatestBinding

class LatestAdapter : BaseRecyclerViewAdapter<Latest, ViewDataBinding>() {

    private var onClickListener: OnClickListener? = null
    var itemAction: ((Item) -> Unit)? = null
    private var fragment: Fragment? = null

    companion object {
        const val ITEM = 0
        const val ADS = 1
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            ADS -> R.layout.item_native_home
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
        when (getItemViewType(position)) {
            ADS -> {
                (holder.binding as? ItemNativeHomeBinding)?.let { bindAds(item, it) }
            }

            else -> {
                (holder.binding as? LayoutItemLatestBinding)?.let { bindItem(item, it) }
            }
        }
    }

    private fun bindItem(item: Latest, binding: LayoutItemLatestBinding) {
        binding.apply {
            imgBackground.loadImage(item.url)
            imgReward.isVisible = item.free != true
            tvHeart.text = item.loves.toString()
            root.setOnSingleClickListener {
                itemAction?.invoke(
                    Item(
                        url = item.url,
                        category = item.category
                    )
                )
            }
        }
    }

    private fun bindAds(item: Latest, binding: ItemNativeHomeBinding) {
        if (item.nativeAd == null) {
            return
        } else {
            binding.shimmerContainerNative1.stopShimmer()
            binding.shimmerContainerNative1.visibility = View.GONE
            binding.flAdplaceholderActivity.visibility = View.VISIBLE
            BkPlusNativeAd.populateNativeAd(fragment, item.nativeAd, binding.flAdplaceholderActivity)
        }
    }

    fun populateNativeAd(nativeAd: BkNativeAd?, fragment: Fragment) {
        this.fragment = fragment
        items.forEachIndexed { index, item ->
            if (item.type == ADS) {
                item.nativeAd = nativeAd
                notifyItemChanged(index)
            }
        }
    }

    fun removeNativeAd() {
        items.removeIf { item -> item.type == ADS }
        updateItems(items)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun actionConnect(item: Latest)
    }
}

