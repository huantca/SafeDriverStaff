package com.bkplus.callscreen.ui.main.category.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.bkplus.callscreen.ultis.loadImage
import com.harison.core.app.platform.BaseRecyclerViewAdapter
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemCategoryBinding
import com.harrison.myapplication.databinding.ItemNativeCategoryBinding

class CategoryAdapter(val onClick: (String) -> Unit) :
    BaseRecyclerViewAdapter<Category, ViewDataBinding>() {
    private var fragment: Fragment? = null

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            LatestAdapter.ADS -> R.layout.item_native_category
            else -> R.layout.item_category
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
                    LatestAdapter.ADS -> 2
                    else -> 1
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewDataBinding, Category>,
        position: Int
    ) {
        bindItem(holder.binding as? ItemCategoryBinding, items.getOrNull(position))
        bindAds(holder.binding as? ItemNativeCategoryBinding, items.getOrNull(position))
    }

    @SuppressLint("SetTextI18n")
    private fun bindItem(binding: ItemCategoryBinding?, item: Category?) {
        binding ?: return
        item ?: return

        binding.name.text = item.name
        binding.number.text = "${item.number} ${binding.root.context.getString(R.string.wallpapers)}"
        binding.image.loadImage(item.thumbnail)
        binding.image.setOnClickListener {
            item.id?.let { it1 -> onClick(it1) }
        }
    }

    private fun bindAds(binding: ItemNativeCategoryBinding?, item: Category?) {
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

    override fun getItemCount(): Int {
        return items.size
    }
}

