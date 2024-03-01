package com.harison.core.app.platform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<T, VB : ViewBinding> :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VB,T>>() {

    var items = arrayListOf<T>()
    var listener: ((view: View, item: T, position: Int) -> Unit)? = null

    /**
     * BaseRecyclerViewAdapter: Submit data by DiffUtil.
     * @param items is ArrayList<T>
     */
    open fun updateItems(items: ArrayList<T>) {
        val taskDiffCallBack = BaseDiffCallBack<T>(this.items, items)
        val diffResult = DiffUtil.calculateDiff(taskDiffCallBack)
        diffResult.dispatchUpdatesTo(this)
        this.items = items
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder<VB,T>(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), getLayoutId(viewType), parent, false
        )
    )

    open class BaseViewHolder<VB : ViewBinding,T>(open val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {
        open fun onBind(item: T, position: Int) {}
    }

    abstract fun getLayoutId(viewType: Int): Int

    fun removeAt(position: Int) {
        if (items.size <= position) items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItem(item: T) {
        val position = items.indexOf(item)
        removeAt(position)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}