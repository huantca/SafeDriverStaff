package com.bkplus.callscreen.ui.setting.language

import android.view.View
import com.bkplus.callscreen.ads.EventTracking
import com.bkplus.callscreen.ads.TrackingManager
import com.bkplus.callscreen.ultis.Language
import com.bkplus.callscreen.ultis.setOnSingleClickListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.LanguageItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class LanguageItem(
    private val language: Language,
    private val onLanguageListener: OnLanguageListener,
    private val viewModel: LanguageViewModel
) : BindableItem<LanguageItemBinding>() {
    private var defaultLocate = "en"

    interface OnLanguageListener {
        fun onChooseLanguage(language: Language, position: Int)
    }

    override fun getLayout(): Int = R.layout.language_item

    override fun initializeViewBinding(view: View): LanguageItemBinding =
        LanguageItemBinding.bind(view)

    override fun bind(viewBinding: LanguageItemBinding, position: Int) {

        viewBinding.languageNameTxt.text = language.name
        viewBinding.flagImg.setImageResource(language.image)

        var locale = defaultLocate
        viewModel.chosenLanguage?.code?.let {
            locale = it
        }

        if (locale == language.code) {
            viewBinding.checkbox.setImageResource(R.drawable.ic_seleted)
        } else {
            viewBinding.checkbox.setImageResource(R.drawable.ic_unchecked)
        }

        viewBinding.root.setOnSingleClickListener {
            onLanguageListener.onChooseLanguage(language, position)
            viewBinding.checkbox.setImageResource(R.drawable.ic_check)
            TrackingManager.tracking(EventTracking.fb003_language_choose_language_click)
        }
    }
}
