package com.bkplus.callscreen.ui.main.favourite


import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callback.BkPlusAdmobInterstitialCallback
import com.ads.bkplus_ads.core.callforward.BkPlusAdmob
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.database.WallpaperEntity
import com.bkplus.callscreen.ui.viewlike.WallPaper
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentFavouriteBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer
    override val layoutId: Int
        get() = R.layout.fragment_favourite

    private var adapter: FavouriteAdapter? = null
    private val viewModel: FavouriteViewModel by viewModels()

    override fun setupData() {
        super.setupData()
        viewModel.getData()
        adapter = FavouriteAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.favouriteList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.updateItems(ArrayList(it))
            }
        }
        adapter?.itemAction = { item, listData ->
            gotoViewLike(item, listData)
        }
    }

    override fun setupUI() {
        super.setupUI()
        loadInterBackHome()
    }

    override fun setupListener() {
        super.setupListener()

        binding.apply {
            backBtn.setOnSingleClickListener {
                showInterBackHome {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun loadInterBackHome() {
        if (BasePrefers.getPrefsInstance().intersitial_backhome) {
            activity?.let {
                if (!adsContainer.isInterAdReady(BuildConfig.intersitial_backhome)) {
                    BkPlusAdmob.loadAdInterstitial(it, BuildConfig.intersitial_backhome,
                        object : BkPlusAdmobInterstitialCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                super.onAdLoaded(interstitialAd)
                                adsContainer.saveInterAd(
                                    BuildConfig.intersitial_backhome,
                                    interstitialAd
                                )
                            }
                        })
                }

            }
        }
    }

    private fun showInterBackHome(action: () -> Unit) {
        if (BasePrefers.getPrefsInstance().intersitial_backhome) {
            activity?.let {
                BkPlusAdmob.showAdInterstitial(it,
                    adsContainer.getInterAd(BuildConfig.intersitial_backhome),
                    object : BkPlusAdmobInterstitialCallback() {
                        override fun onShowAdRequestProgress(tag: String, message: String) {
                            super.onShowAdRequestProgress(tag, message)
                            action.invoke()
                        }

                        override fun onAdFailed(tag: String, errorMessage: String) {
                            super.onAdFailed(tag, errorMessage)
                            adsContainer.removeInterAd(BuildConfig.intersitial_backhome)
                        }

                        override fun onAdDismissed(tag: String, message: String) {
                            super.onAdDismissed(tag, message)
                            adsContainer.removeInterAd(BuildConfig.intersitial_backhome)
                        }
                    })
            }
        } else {
            action.invoke()
        }
    }

    private fun gotoViewLike(item: WallpaperEntity, listData: ArrayList<WallpaperEntity>) {
        val wallpaper = WallPaper(
            id = item.id?.toInt(),
            url = item.imageUrl,
            likeCount = item.loves,
            free = item.free
        )
        val listItem = listData.map { dataItem ->
            WallPaper(
                id = dataItem.id?.toInt(),
                url = dataItem.imageUrl,
                likeCount = dataItem.loves,
                free = dataItem.free
            )
        }.toTypedArray()
        findNavController().navigate(
            FavouriteFragmentDirections.actionFavouriteFragmentToViewLikeContainerFragment(
                wallpaper,
                listItem
            )
        )
    }

}