package com.bkplus.callscreen.ui.viewlike

import android.content.res.Resources
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeContainerBinding

class ViewLikeContainerFragment : BaseFragment<FragmentViewLikeContainerBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_view_like_container

    private val snapHelper = LinearSnapHelper()
    val list = arrayListOf<WallPaper>(
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        ),
        WallPaper(
            isLiked = false,
            id = 0,
            url = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg",
            likes = 10
        )
    )

    override fun setupUI() {
        super.setupUI()

        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity(), list)
        binding.viewPager.adapter = pagerAdapter
        initViewPager()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity, val items: List<WallPaper>) :
        FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = items.size

        override fun createFragment(position: Int): Fragment {
            val fragment = ViewLikeItemFragment()
            fragment.initData(items[position])
            return fragment
        }
    }

    private fun initViewPager() {
        binding.viewPager.apply {
            isSaveEnabled = false
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 1 // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((20 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = (0.75f + r * 0.20f)
        }
        binding.viewPager.setPageTransformer(compositePageTransformer)
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            }
        })
    }

}