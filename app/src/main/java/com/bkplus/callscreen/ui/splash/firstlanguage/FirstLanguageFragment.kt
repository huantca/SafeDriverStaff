package com.bkplus.callscreen.ui.splash.firstlanguage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.callscreen.common.BaseFragment
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.ui.setting.language.LanguageItem
import com.bkplus.callscreen.ui.setting.language.LanguageViewModel
import com.bkplus.callscreen.ultis.Constants
import com.bkplus.callscreen.ultis.ContextUtils
import com.bkplus.callscreen.ultis.Language
import com.bkplus.callscreen.ultis.MyContextWrapper
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentFirstLanguageBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import java.util.Locale

class FirstLanguageFragment : BaseFragment<FragmentFirstLanguageBinding>(), LanguageItem.OnLanguageListener {
    override val layoutId: Int
        get() = R.layout.fragment_first_language


    private lateinit var groupLanguage: GroupAdapter<GroupieViewHolder>
    private lateinit var locales: ArrayList<Language>

    private val languageViewModel: LanguageViewModel by viewModels()
    private var newLocale: String? = null

    override fun setupData() {
        super.setupData()
        groupLanguage = GroupAdapter()
        groupLanguage.clear()
        initLanguage()
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

}
