package com.bkplus.android.ui.splash.welcome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ItemRecyclerWelcomeBinding

class WelcomeAdapter : RecyclerView.Adapter<WelcomeAdapter.WelcomeViewHolder>() {

    private val items = arrayListOf<Category>()

    private var onItemClickListener: () -> Unit = {}

    fun setOnItemClickListener(onItemClickListener: () -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Category>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class WelcomeViewHolder(private val binding: ItemRecyclerWelcomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.apply {
                Glide.with(root.context).load(item.icon).into(binding.image)
                name.text = item.name
                selectedView.isVisible = item.isSelected
                checkbox.setImageResource(if (item.isSelected) R.drawable.ic_welcome_checkbox_checked else R.drawable.ic_welcome_checkbox_unchecked)
                container.clipToOutline = true
                container.setOnClickListener {
                    item.isSelected = !item.isSelected
                    onItemClickListener.invoke()
                    notifyItemChanged(bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        return WelcomeViewHolder(
            ItemRecyclerWelcomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        items.getOrNull(position)?.let { holder.bind(it) }
    }

    fun hasSelectedItem(): Boolean {
        return items.any { item -> item.isSelected }
    }
}
