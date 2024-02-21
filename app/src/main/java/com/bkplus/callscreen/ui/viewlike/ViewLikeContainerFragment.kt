package com.bkplus.callscreen.ui.viewlike

import android.app.WallpaperManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.widget.CongratulationsDialog
import com.bkplus.callscreen.ui.widget.SetWallpaperBottomSheet
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewLikeContainerFragment : BaseFragment<FragmentViewLikeContainerBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_view_like_container

    var list = arrayListOf<WallPaper>()
    private var currentPosition = 0
    private val args by navArgs<ViewLikeContainerFragmentArgs>()
    private val viewModel : ViewLikeViewModel by viewModels()

    override fun setupData() {
        super.setupData()
        list.clear()
        list.addAll(args.listData)
        viewModel.matchWallpaperToDB(list)
        val pos = args.listData.indexOfFirst {
            it.url == args.item.url
        }
        currentPosition = if (pos >= 0) pos else 0
    }

    override fun setupUI() {
        super.setupUI()
        val pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager, lifecycle, list)
        binding.viewPager.adapter = pagerAdapter
        initViewPager()
    }

    private inner class ScreenSlidePagerAdapter(
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
                Log.d(this@ViewLikeContainerFragment.javaClass.simpleName, "onPageScrolled: $position")
                currentPosition = position
            }
        })
        binding.viewPager.setCurrentItem(currentPosition, true)
    }

    private fun goToSuccess() {
        val containerFragment = CongratulationsDialog().apply {
            actionHome = {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
            actionBack = {

            }
        }
        containerFragment.show(childFragmentManager, "")
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            setWallpaperBtn.setOnSingleClickListener {
                setWallpaper()
            }

            backBtn.setOnSingleClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setWallpaper() {
        requireContext { ct ->
            val currentImage = list.getOrNull(binding.viewPager.currentItem)
            Glide.with(ct)
                .asBitmap()
                .load(currentImage?.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        SetWallpaperBottomSheet.newInstance(
                            onClickSetLockScreen = {
                                setLockScreen(resource)
                            },
                            onClickSeHomeScreen = {
                                setWallpaper(resource)
                            },
                            onClickSetBothScreen = {
                                setBothWallpaper(resource)
                            }
                        ).show(childFragmentManager)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                    }
                })
        }
    }

    fun setWallpaper(bitmap: Bitmap) {
        requireContext { ct ->
            try {
                WallpaperManager.getInstance(ct).setBitmap(bitmap)
                toast(getString(R.string.set_wallpaper))
                goToSuccess()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    fun setLockScreen(bitmap: Bitmap) {
        requireContext { ct ->
            try {
                WallpaperManager.getInstance(ct)
                    .setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                toast(getString(R.string.set_wallpaper))
                goToSuccess()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    fun setBothWallpaper(bitmap: Bitmap) {
        requireContext { ct ->
            try {
                WallpaperManager.getInstance(ct).setBitmap(bitmap)
                WallpaperManager.getInstance(ct)
                    .setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                toast(getString(R.string.set_wallpaper))
                goToSuccess()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }
}