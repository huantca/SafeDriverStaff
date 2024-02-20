package com.bkplus.callscreen.ui.main.home.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.api.onSuccess
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao,
    private val apiService: ApiService
) : ViewModel() {
    val list = MutableLiveData<List<Item>>()
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
           apiService.getApiData().onSuccess {

           }
        }
    }
}