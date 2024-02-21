package com.bkplus.callscreen.ui.main.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.onSuccess
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import com.bkplus.callscreen.ui.main.home.adapter.LatestAdapter
import com.harison.core.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val wallpaperDao: WallpaperDao
) : ViewModel() {

    val homeSectionLiveData = MutableLiveData<ArrayList<HomeSectionEntity>>()
    val categories = MutableLiveData<ArrayList<Category>>()
    val homeSectionAndCategoryLiveData = MutableLiveData<Boolean>()

    init {
        fakeDataBase()
    }

    fun getHomeSection() {
        viewModelScope.launch {
            apiService.getApiData().onSuccess {
                homeSectionLiveData.postValue(it)
            }
        }
    }

    fun getSearch() {
        viewModelScope.launch {
            apiService.getApiData().onSuccess { list ->
                homeSectionLiveData.postValue(list)
                if (categories.value != null) homeSectionAndCategoryLiveData.postValue(true)
            }
            apiService.getCategoryData().onSuccess {
                categories.postValue(it)
                if (homeSectionLiveData.value != null) homeSectionAndCategoryLiveData.postValue(true)
            }
        }
    }

    fun addNativeAd(categories: List<Category>): ArrayList<Category> {
        val result = arrayListOf<Category>()
        categories.forEachIndexed { index, category ->
            result.add(category)
            if ((index + 1) % 4 == 0) {
                result.add(Category(type = LatestAdapter.ADS))
            }
        }
        return result
    }

    fun getCategory() {
        viewModelScope.launch {
            apiService.getCategoryData().onSuccess {
                categories.postValue(it)
            }
        }
    }

    private fun fakeDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.tag("WallpaperDao").d(wallpaperDao.getCount().toString())
            wallpaperDao.getCount().apply {
                if (this == 0) {
                    val list = arrayListOf(
                        WallpaperEntity(
                            generateId = 0,
                            id = "1",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        )
                    )
                    wallpaperDao.insertAll(list)
                }
            }
        }
    }
}
