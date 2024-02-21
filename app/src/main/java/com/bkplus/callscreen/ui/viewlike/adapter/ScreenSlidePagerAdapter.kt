package com.bkplus.callscreen.ui.viewlike.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bkplus.callscreen.ui.viewlike.ViewLikeItemFragment
import com.bkplus.callscreen.ui.viewlike.WallPaper

class ScreenSlidePagerAdapter(
    fa: FragmentManager,
    lifecycle: Lifecycle,
    val items: List<WallPaper>
) :
    FragmentStateAdapter(fa, lifecycle) {
        override fun getItemCount(): Int = items.size

        override fun createFragment(position: Int): Fragment {
            val fragment = ViewLikeItemFragment()
            fragment.initData(items[position])
            return fragment
        }
}