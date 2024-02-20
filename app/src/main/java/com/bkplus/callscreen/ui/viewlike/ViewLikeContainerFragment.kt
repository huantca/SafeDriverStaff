package com.bkplus.callscreen.ui.viewlike

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Movie
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ui.main.home.model.Latest
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeContainerBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@Suppress("DEPRECATION")
class ViewLikeContainerFragment : BaseFragment<FragmentViewLikeContainerBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_view_like_container

    private var currentPosition: Int = -1
    private val coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineExceptionHandler { _, _ ->
        })
    private var ct: Context? = null

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

    override fun setupListener() {
        super.setupListener()

        binding.apply {
            setWallpaperBtn.setOnSingleClickListener {
                //if (currentPosition == -1) currentPosition = args.position
                if (currentPosition > list.size) {
                    Toast.makeText(context, "An error occurred, please try again", Toast.LENGTH_LONG).show()
                } else {
                    val currentImage = list[currentPosition]
                    setWallpaper(currentImage)
                }
            }

            backBtn.setOnSingleClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity, val items: List<WallPaper>) :
        FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = items.size

        override fun createFragment(position: Int): Fragment {
            val fragment = ViewLikeItemFragment()
            fragment.initData(items[position])
            currentPosition = position
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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ct = context
    }

    @SuppressLint("MissingPermission")
    fun setWallpaper(image: WallPaper) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val myWallpaperManager = WallpaperManager.getInstance(ct)
                val bm = BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                    URL(image.url).openStream()
                })

                myWallpaperManager.setBitmap(bm)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    getString(R.string.an_error_has_occurred_when_install_this_wallpaper_please_try_again_later),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            goToSuccess()
        }
    }

    private fun goToSuccess() {
        findNavController().navigate(R.id.congratulationsFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}