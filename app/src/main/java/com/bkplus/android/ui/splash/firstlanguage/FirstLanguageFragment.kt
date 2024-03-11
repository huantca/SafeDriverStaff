package com.bkplus.android.ui.splash.firstlanguage

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentFirstLanguageBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
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


}
