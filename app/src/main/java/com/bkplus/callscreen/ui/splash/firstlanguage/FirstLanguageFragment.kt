package com.bkplus.callscreen.ui.splash.firstlanguage

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.bkplus_ads.core.callback.BkPlusNativeAdCallback
import com.ads.bkplus_ads.core.callforward.BkPlusNativeAd
import com.ads.bkplus_ads.core.model.BkNativeAd
import com.bkplus.callscreen.ads.AdsContainer
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.setting.language.LanguageItem
import com.bkplus.callscreen.ui.setting.language.LanguageViewModel
import com.bkplus.callscreen.ultis.Constants
import com.bkplus.callscreen.ultis.ContextUtils
import com.bkplus.callscreen.ultis.Language
import com.bkplus.callscreen.ultis.MyContextWrapper
import com.bkplus.callscreen.ultis.gone
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.bkplus.callscreen.ultis.visible
import com.google.android.gms.ads.LoadAdError
import com.harrison.myapplication.BuildConfig
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentFirstLanguageBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

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
