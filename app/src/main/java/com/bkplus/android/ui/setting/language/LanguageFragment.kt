package com.bkplus.android.ui.setting.language

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.ultis.Constants.en
import com.bkplus.android.ultis.ContextUtils
import com.bkplus.android.ultis.Language
import com.bkplus.android.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentLanguageBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import java.util.Locale

class LanguageFragment : BaseFragment<FragmentLanguageBinding>(), LanguageItem.OnLanguageListener {

    override val layoutId: Int
        get() = R.layout.fragment_language

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
            newLocale = languageViewModel.chosenLanguage?.code ?: Locale.getDefault().displayLanguage
            val locale = BasePrefers.getPrefsInstance().locale
            if (locale != newLocale) {
                BasePrefers.getPrefsInstance().locale = newLocale ?: en
            }
            refreshLayout()
        }

        binding.backBtn.setOnSingleClickListener {
            findNavController().navigateUp()
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

    private fun refreshLayout() {
        val intent = activity?.intent
        intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        activity?.finish()
        intent?.let { startActivity(it) }
    }

}
