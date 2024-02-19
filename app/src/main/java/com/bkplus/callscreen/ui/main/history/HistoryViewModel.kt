package com.bkplus.callscreen.ui.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.database.WallpaperDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao
) : ViewModel() {
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDao.getAll().collect {
                Timber.e("---- ${it.size}")
            }
        }
    }

}