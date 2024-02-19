package com.bkplus.callscreen.ui.main.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao
) : ViewModel() {
    val list = MutableLiveData<List<WallpaperEntity>>()
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDao.getAll().collect {
                Timber.tag("WallpaperDao").e("---- ${it.size}")
                list.postValue(it)
            }
        }
    }

    fun delete(item: WallpaperEntity) {
        wallpaperDao.deleteItem(item.generateId)
    }
}
