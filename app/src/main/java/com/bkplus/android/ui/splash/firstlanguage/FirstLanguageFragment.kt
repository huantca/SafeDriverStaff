package com.bkplus.android.ui.splash.firstlanguage

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.ads.EventTracking
import com.bkplus.android.ads.TrackingManager
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ui.setting.language.LanguageItem
import com.bkplus.android.ui.setting.language.LanguageViewModel
import com.bkplus.android.ultis.ContextUtils
import com.bkplus.android.ultis.Language
import com.bkplus.android.ultis.MyContextWrapper
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.setOnSingleClickListener
import com.bkplus.android.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentFirstLanguageBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class FirstLanguageFragment : BaseFragment<FragmentFirstLanguageBinding>(), LanguageItem.OnLanguageListener {
    override val layoutId: Int
        get() = R.layout.fragment_first_language

    @Inject
    lateinit var adsContainer: AdsContainer

    private lateinit var groupLanguage: GroupAdapter<GroupieViewHolder>
    private lateinit var locales: ArrayList<Language>

    private val languageViewModel: LanguageViewModel by viewModels()
    private var newLocale: String? = null

    private var canReloadAd = false

    override fun setupData() {
        super.setupData()
        groupLanguage = GroupAdapter()
        groupLanguage.clear()
        initLanguage()
        showNativeAdIfReady()
    }

    override fun onStart() {
        super.onStart()
        if (BasePrefers.getPrefsInstance().native_language && canReloadAd) {
            reloadNativeAd()
        }
        canReloadAd = true
    }

    override fun setupUI() {
        super.setupUI()
        TrackingManager.tracking(EventTracking.fb011_LFO_view)
        binding.apply {
            languageSettingRcv.apply {
                setHasFixedSize(false)
                requireContext {
                    layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
                }
                adapter = groupLanguage
            }
        }
    }

    override fun setupListener() {
        super.setupListener()

        binding.confirmLanguage.setOnSingleClickListener {
            TrackingManager.tracking(EventTracking.fb011_language_choose_language_v_click)
            newLocale =
                languageViewModel.chosenLanguage?.code ?: Locale.getDefault().displayLanguage
            if (newLocale != null) {
                BasePrefers.getPrefsInstance().locale =
                    newLocale ?: Locale.getDefault().displayLanguage
                requireContext {
                    val wrapContext = MyContextWrapper.wrap(
                        it, newLocale!!
                    )
                    resources.updateConfiguration(
                        wrapContext.resources.configuration, wrapContext.resources.displayMetrics
                    )
                }
            }
            goToOnboarding()
        }
    }

    private fun initLanguage() {
        locales = ContextUtils.getLocalesListFirt(resources)
        languageViewModel.chosenLanguage =
            locales.firstOrNull { language: Language -> language.code == BasePrefers.getPrefsInstance().locale }
        locales.forEach {
            groupLanguage.add(LanguageItem(it, this, languageViewModel))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onChooseLanguage(language: Language, position: Int) {
        languageViewModel.chosenLanguage = language
        groupLanguage.notifyDataSetChanged()
    }

    private fun goToOnboarding() {
        BasePrefers.getPrefsInstance().newUser = false
        findNavController().navigate(R.id.onboardFragment)
    }

    private fun reloadNativeAd() {
        if (BasePrefers.getPrefsInstance().native_language) {
            Timber.d("reload native ad")
            binding.flAdplaceholderActivity.removeAllViews()
            binding.shimmerContainerNative1.startShimmer()
            binding.shimmerContainerNative1.visibility = View.VISIBLE
            BkPlusNativeAd.loadNativeAd(
                activity,
                BuildConfig.native_language,
                R.layout.native_first_language,
                object : BkPlusNativeAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: BkNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        adsContainer.nativeFirstLanguage.postValue(nativeAd)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        adsContainer.nativeFirstLanguage.postValue(error)
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        TrackingManager.tracking(EventTracking.fb011_click_ads_native_language)
                    }
                })
        }
    }

    private fun showNativeAdIfReady() {
        if (BasePrefers.getPrefsInstance().native_language) {
            adsContainer.nativeFirstLanguage.observe(viewLifecycleOwner) {
                when (it) {
                    is LoadAdError -> removeNativeAd()
                    is BkNativeAd -> populateNativeAd(it)
                }
            }
        } else {
            removeNativeAd()
        }
    }

    private fun populateNativeAd(nativeAd: BkNativeAd) {
        binding.shimmerContainerNative1.gone()
        binding.shimmerContainerNative1.stopShimmer()
        binding.flAdplaceholderActivity.visible()
        BkPlusNativeAd.populateNativeAd(this, nativeAd, binding.flAdplaceholderActivity)
    }

    private fun removeNativeAd() {
        binding.shimmerContainerNative1.stopShimmer()
        binding.shimmerContainerNative1.gone()
        binding.frNativeAds.gone()
    }
}
