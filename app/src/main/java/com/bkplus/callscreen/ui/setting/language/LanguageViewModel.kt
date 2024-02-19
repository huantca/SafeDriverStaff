package com.bkplus.callscreen.ui.setting.language

import androidx.lifecycle.ViewModel
import com.bkplus.callscreen.ultis.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(): ViewModel() {
    var chosenLanguage: Language? = null
}