package com.bkplus.callscreen.ui.main.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.entity.Category
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.onSuccess
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import com.harison.core.app.utils.SingleLiveData
import com.harrison.myapplication.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val wallpaperDao: WallpaperDao
) : ViewModel() {

    val homeSectionLiveData = SingleLiveData<ArrayList<HomeSectionEntity>>()
    val categories = SingleLiveData<ArrayList<Category>>()
    val homeSectionAndCategoryLiveData = SingleLiveData<Boolean>()

    init {
        if (BuildConfig.DEBUG) fakeDataBase()
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
                        ),
                        WallpaperEntity(
                            generateId = 1,
                            id = "2",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 2,
                            id = "3",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 3,
                            id = "4",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 4,
                            id = "5",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 5,
                            id = "6",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 6,
                            id = "7",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 7,
                            id = "8",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 8,
                            id = "9",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                        WallpaperEntity(
                            generateId = 9,
                            id = "10",
                            imageUrl = "https://i.pinimg.com/736x/39/11/6c/39116c247669762f4ce72be4ce2b862e.jpg"
                        ),
                    )
                    wallpaperDao.insertAll(list)
                }
            }
        }
    }
}
