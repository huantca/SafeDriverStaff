package com.bkplus.callscreen.ui.viewlike

import androidx.fragment.app.viewModels
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeItemBinding
import dagger.hilt.android.AndroidEntryPoint
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.openShare
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig

@AndroidEntryPoint
class ViewLikeItemFragment : BaseFragment<FragmentViewLikeItemBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_view_like_item

    private var wallPaper: WallPaper? = null
    private val viewModel: ViewLikeViewModel by viewModels()
    private var likeCountLive: Int? = null

    fun initData(item: WallPaper) {
        wallPaper = item
        likeCountLive = wallPaper?.likeCount
    }

    override fun setupUI() {
        super.setupUI()
        if (wallPaper?.isAds == true){
            binding.ctlItem.gone()
            binding.frNativeAd.visible()
            loadNativeAd()
        }else{
            binding.frNativeAd.gone()
            binding.ctlItem.visible()
            wallPaper?.let { item ->
                Glide.with(binding.wallPaperImage.context).load(item.url)
                    .into(binding.wallPaperImage)
            }

            binding.apply {
                likeCounts.text = wallPaper?.likeCount.toString()
                if (wallPaper?.isLiked == false) likeBtn.setImageResource(R.drawable.ic_heart_unfill)
                else likeBtn.setImageResource(R.drawable.ic_heart_fill)
            }
        }
        binding.apply {
            likeCounts.text = likeCountLive.toString()
            if (wallPaper?.isLiked == false) likeBtn.setImageResource(R.drawable.ic_heart_unfill)
            else likeBtn.setImageResource(R.drawable.ic_heart_fill)
        }
    }

    }

    override fun setupListener() {
        super.setupListener()

        binding.apply {

            likeBtn.setOnSingleClickListener {
                handleLikeAction()
            }

            shareBtn.setOnSingleClickListener {
                activity.openShare()
            }

            previewBtn.setOnSingleClickListener {
                PreviewDialogFragment.newInstance(
                    item = wallPaper,
                    onDismiss = { }
                ).show(childFragmentManager, "")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleLikeAction() {
        binding.apply {
                if (wallPaper?.isLiked == true) {
                    likeBtn.setImageResource(R.drawable.ic_heart_unfill)
                    wallPaper?.isLiked = false
                    likeCountLive = likeCountLive?.minus(1)
                    viewModel.disperse(wallPaper)
                } else {
                    likeBtn.setImageResource(R.drawable.ic_heart_fill)
                    wallPaper?.isLiked = true
                    likeCountLive = likeCountLive?.plus(1)
                    viewModel.saveFavourite(wallPaper)
                }
            likeCounts.text = likeCountLive.toString()
            likeCountLive = likeCounts.text.toString().toInt()
        }
    }

    private fun loadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_viewwallpaper) {
            BkPlusNativeAd.loadNativeAd(
                this,
                BuildConfig.native_viewwallpaper,
                R.layout.layout_native_view_like,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        populateNativeAd(nativeAd)
                    }
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        removeNativeAd()
                    }
                })

        } else {
            removeNativeAd()
        }

    }

    private fun populateNativeAd(nativeAd: BkNativeAd) {
        binding.shimmerContainerNativeExit.gone()
        binding.shimmerContainerNativeExit.stopShimmer()
        binding.flAdplaceholderExit.visible()
        BkPlusNativeAd.populateNativeAd(this, nativeAd, binding.flAdplaceholderExit)
    }

    private fun removeNativeAd() {
        binding.shimmerContainerNativeExit.stopShimmer()
        binding.shimmerContainerNativeExit.gone()
        binding.frNativeAd.gone()
    }
}