package com.bkplus.callscreen.ui.main.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao
) : ViewModel() {

    val favouriteList = MutableLiveData<List<WallpaperEntity>>()

    fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                wallpaperDao.getLiked().collect { listLiked ->
                    favouriteList.postValue(listLiked)
                }
            }
        }
    }
    fun freeItem(item: Item) {
        val freeItems = BasePrefers.getPrefsInstance().listItemsFree
        freeItems.add(item)
        BasePrefers.getPrefsInstance().listItemsFree = freeItems
    }
}
