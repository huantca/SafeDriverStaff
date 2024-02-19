package com.bkplus.callscreen.ui.viewlike

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.database.WallpaperDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewLikeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val wallpaperDao: WallpaperDao
) : ViewModel() {

    var wpList: ArrayList<Item> = arrayListOf()
    var positionClick: Int = 0
    private var loadWPList = MutableLiveData<ArrayList<Item>>()

    fun loadListObservable(): MutableLiveData<ArrayList<Item>> {
        return loadWPList
    }

    private fun transformTrendingList(data: ArrayList<Item>) {

    }
}