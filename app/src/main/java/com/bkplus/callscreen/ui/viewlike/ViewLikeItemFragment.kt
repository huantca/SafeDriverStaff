package com.bkplus.callscreen.ui.viewlike

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.viewModels
import com.ads.bkplus_ads.core.callback.BkPlusAdmobRewardedCallback
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.ads.bkplus_ads.core.toastDebug
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.widget.RewardDialog
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.openShare
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bkplus.callscreen.ultis.visible
import com.bumptech.glide.Glide
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentViewLikeItemBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewLikeItemFragment : BaseFragment<FragmentViewLikeItemBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer

    override val layoutId: Int
        get() = R.layout.fragment_view_like_item

    private var wallPaper: WallPaper? = null
    private val viewModel: ViewLikeViewModel by viewModels()
    private var likeCountLive: Int? = null

    private var actionShowButtonWallpaper: (() -> Unit)? = null

    fun setActionShowButton(action: () -> Unit) {
        this.actionShowButtonWallpaper = action
    }

    fun initData(item: WallPaper) {
        wallPaper = item
        likeCountLive = wallPaper?.likeCount
    }

    override fun setupUI() {
        super.setupUI()
        if (wallPaper?.isAds == true) {
            binding.ctlItem.gone()
            binding.ctlAds.gone()
            binding.frNativeAd.visible()
            loadNativeAd()
        } else {
            loadAdReward()
            BasePrefers.getPrefsInstance().listItemsFree.forEach {
                if (it.url == wallPaper?.url) {
                    wallPaper?.free = true
                    return@forEach
                }
            }

            if (!BasePrefers.getPrefsInstance().reward_gif) {
                wallPaper?.free = true
            }

            if (wallPaper?.free == true) {
                binding.frNativeAd.gone()
                binding.ctlAds.gone()
                binding.ctlItem.visible()
            } else {
                binding.frNativeAd.gone()
                binding.serviceBar.gone()
                binding.ctlAds.visible()
                binding.ctlAds.setOnSingleClickListener {
                    RewardDialog().apply {
                        action = {
                            showRewardAd {
                                hideBgAds()
                                actionShowButtonWallpaper?.invoke()
                                val item = Item(id = wallPaper?.id, url = wallPaper?.url)
                                viewModel.freeItem(item)
                            }
                        }
                    }.show(childFragmentManager, "")
                }
            }

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
        }
    }

    private fun hideBgAds() {
        binding.frNativeAd.gone()
        binding.serviceBar.visible()
        binding.ctlAds.gone()
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

    private fun loadAdReward() {
        if (BasePrefers.getPrefsInstance().reward_gif) {
            activity?.let {
                if (!adsContainer.isRewardedAdReady(BuildConfig.reward_gif)) {
                    BkPlusAdmob.loadAdRewarded(it, BuildConfig.reward_gif,
                        object : BkPlusAdmobRewardedCallback() {
                            override fun onRewardAdLoaded(rewardedAd: RewardedAd) {
                                super.onRewardAdLoaded(rewardedAd)
                                Log.e("huan", "onRewardAdLoaded")
                                adsContainer.saveRewardedAd(BuildConfig.reward_gif, rewardedAd)
                                toastDebug(context, "load reward")
                            }
                        })
                }

            }
        }
    }

    private fun showRewardAd(action: () -> Unit) {
        if (BasePrefers.getPrefsInstance().reward_gif) {
            activity?.let {
                BkPlusAdmob.showAdRewarded(it, adsContainer.getRewardedAd(BuildConfig.reward_gif),
                    object : BkPlusAdmobRewardedCallback() {
                        override fun onShowAdRequestProgress(tag: String, message: String) {
                            super.onShowAdRequestProgress(tag, message)
                            action.invoke()
                        }

                        override fun onAdFailed(tag: String, errorMessage: String) {
                            super.onAdFailed(tag, errorMessage)
                            toast(getString(R.string.load_ads_fail), false)
                            adsContainer.removeRewardedAd(BuildConfig.reward_gif)
                        }

                        override fun onAdDismissed(tag: String, message: String) {
                            super.onAdDismissed(tag, message)
                            adsContainer.removeRewardedAd(BuildConfig.reward_gif)
                        }
                    })

            }
        } else {
            action.invoke()
        }
    }
}