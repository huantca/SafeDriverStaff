package com.bkplus.callscreen.ui.viewlike

import android.app.WallpaperManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.viewlike.adapter.ScreenSlidePagerAdapter
import com.bkplus.callscreen.ui.widget.SetWallpaperBottomSheet
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeContainerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ViewLikeContainerFragment : BaseFragment<FragmentViewLikeContainerBinding>() {

    @Inject
    lateinit var adsContainer : AdsContainer
    override val layoutId: Int
        get() = R.layout.fragment_view_like_container

    var list = arrayListOf<WallPaper>()
    private var currentPosition = 0
    private val args by navArgs<ViewLikeContainerFragmentArgs>()
    private val viewModel: ViewLikeViewModel by viewModels()

    override fun setupData() {
        super.setupData()
        list.clear()
        args.listData.forEachIndexed { index, wallPaper ->
            list.add(wallPaper)
            if (index % 6 == 0 && BasePrefers.getPrefsInstance().native_viewwallpaper) {
                list.add(WallPaper(isAds = true))
            }
        }

        viewModel.matchWallpaperToDB(list)
        val pos = list.indexOfFirst {
            it.url == args.item.url
        }
        currentPosition = if (pos >= 0) pos else 0
    }

    override fun setupUI() {
        super.setupUI()
        loadInterSetWallPaper()
        val pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager, lifecycle, list, action = {
            binding.setWallpaperBtn.visibility = View.VISIBLE
        })
        binding.viewPager.adapter = pagerAdapter
        initViewPager()
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
                Timber.tag(this.javaClass.simpleName).d("onPageScrolled: %s", position)
                if (list.getOrNull(position)?.isAds == true || list.getOrNull(position)?.free != true) {
                    binding.setWallpaperBtn.visibility = View.INVISIBLE
                } else {
                    binding.setWallpaperBtn.visibility = View.VISIBLE
                }
                BasePrefers.getPrefsInstance().listItemsFree.forEach {
                    if (list.getOrNull(position)?.url == it.url){
                        binding.setWallpaperBtn.visibility = View.VISIBLE
                        return@forEach
                    }
                }

                currentPosition = position
            }
        })
        binding.viewPager.setCurrentItem(currentPosition, false)
    }

    private fun goToSuccess() {
        toast(getString(R.string.set_wallpaper))
        showInterSetWallPaper {
            val bundle = Bundle()
            bundle.putInt("fragment",args.destination)
            findNavController().navigate(R.id.CongratulationFragment,bundle)
        }

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
                            },
                            onClickSetWallpaper = {
                                viewModel.saveHistory(currentImage)
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
                lifecycleScope.launch {
                    showLoading()
                    withContext(Dispatchers.IO) {
                        WallpaperManager.getInstance(ct).setBitmap(bitmap)
                    }

                }
                hideLoading()
                goToSuccess()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }

    }

    fun setLockScreen(bitmap: Bitmap) {
        requireContext { ct ->
            try {
                lifecycleScope.launch {
                    showLoading()
                    withContext(Dispatchers.IO) {
                        WallpaperManager.getInstance(ct)
                            .setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    }
                }
                hideLoading()
                goToSuccess()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    fun setBothWallpaper(bitmap: Bitmap) {
        requireContext { ct ->
            try {
                lifecycleScope.launch {
                    showLoading()
                    withContext(Dispatchers.IO) {
                        WallpaperManager.getInstance(ct).setBitmap(bitmap)
                        WallpaperManager.getInstance(ct)
                            .setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    }
                }
                hideLoading()
                goToSuccess()
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }


    private fun loadInterSetWallPaper() {
        if (BasePrefers.getPrefsInstance().intersitial_setwallpaper) {
            activity?.let {
                if (!adsContainer.isInterAdReady(BuildConfig.intersitial_setwallpaper)) {
                    BkPlusAdmob.loadAdInterstitial(it, BuildConfig.intersitial_setwallpaper,
                        object : BkPlusAdmobInterstitialCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                adsContainer.saveInterAd(
                                    BuildConfig.intersitial_setwallpaper,
                                    interstitialAd
                                )
                            }
                        })
                }

            }
        }
    }

    private fun showInterSetWallPaper(action: () -> Unit) {
        if (BasePrefers.getPrefsInstance().intersitial_setwallpaper) {
            activity?.let {
                BkPlusAdmob.showAdInterstitial(it,
                    adsContainer.getInterAd(BuildConfig.intersitial_setwallpaper),
                    object : BkPlusAdmobInterstitialCallback() {
                        override fun onShowAdRequestProgress(tag: String, message: String) {
                            super.onShowAdRequestProgress(tag, message)
                            action.invoke()
                        }

                        override fun onAdFailed(tag: String, errorMessage: String) {
                            super.onAdFailed(tag, errorMessage)
                            adsContainer.removeInterAd(BuildConfig.intersitial_setwallpaper)
                        }

                        override fun onAdDismissed(tag: String, message: String) {
                            super.onAdDismissed(tag, message)
                            adsContainer.removeInterAd(BuildConfig.intersitial_setwallpaper)
                        }
                    })
            }
        } else {
            action.invoke()
        }
    }
}