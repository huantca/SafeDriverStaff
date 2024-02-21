package com.bkplus.callscreen.ui.main.home.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.onSuccess
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao,
    private val apiService: ApiService
) : ViewModel() {
    val favouriteList = MutableLiveData<List<WallpaperEntity>>()
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
           wallpaperDao.getAll().collect {
               val list = it.filter { item -> item.isLike == true }
               favouriteList.postValue(list)
           }
        }
    }
}