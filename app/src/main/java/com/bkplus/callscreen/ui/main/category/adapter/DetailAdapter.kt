package com.bkplus.callscreen.ui.main.category.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemCategoryDetailBinding
import com.harrison.myapplication.databinding.ItemNativeHomeBinding

class DetailAdapter : BaseRecyclerViewAdapter<Item, ViewDataBinding>() {

    var onItemRcvClick: (Item, listData: ArrayList<Item>) -> Unit? =
        { _: Item, _: ArrayList<Item> -> }
    private var fragment: Fragment? = null

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            LatestAdapter.ADS -> R.layout.item_native_home
            else -> R.layout.item_category_detail
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items.getOrNull(position)?.type ?: LatestAdapter.ITEM
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()
    }

    private fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (items[position].type) {
                    LatestAdapter.ADS -> 3
                    else -> 1
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewDataBinding, Item>,
        position: Int
    ) {
        bindItem(holder.binding as? ItemCategoryDetailBinding, items.getOrNull(position))
        bindAds(holder.binding as? ItemNativeHomeBinding, items.getOrNull(position))
    }

    @SuppressLint("SetTextI18n")
    private fun bindItem(binding: ItemCategoryDetailBinding?, item: Item?) {
        binding ?: return
        item ?: return

        BasePrefers.getPrefsInstance().listItemsFree.forEach {
            if (it.url == item.url) {
                item.free = true
                return@forEach
            }
        }
        binding.image.loadImage(item.thumbnail)
        binding.tvHeart.text = item.loves.toString()
        binding.icReward.isVisible = item.free != true
        binding.image.setOnClickListener {
            onItemRcvClick.invoke(item, items)
        }
    }

    private fun bindAds(binding: ItemNativeHomeBinding?, item: Item?) {
        binding ?: return
        item?.nativeAd ?: return

        binding.shimmerContainerNative1.stopShimmer()
        binding.shimmerContainerNative1.visibility = View.GONE
        binding.flAdplaceholderActivity.visibility = View.VISIBLE
        BkPlusNativeAd.populateNativeAd(fragment, item.nativeAd, binding.flAdplaceholderActivity)
    }

    fun populateNativeAd(nativeAd: BkNativeAd?, fragment: Fragment) {
        this.fragment = fragment
        items.forEachIndexed { index, item ->
            if (item.type == LatestAdapter.ADS) {
                item.nativeAd = nativeAd
                notifyItemChanged(index)
            }
        }
    }

    fun removeNativeAd() {
        items.removeIf { item -> item.type == LatestAdapter.ADS }
        updateItems(items)
    }
}
