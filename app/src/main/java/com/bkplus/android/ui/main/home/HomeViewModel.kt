package com.bkplus.android.ui.main.home

import androidx.lifecycle.ViewModel
import com.bkplus.android.api.ApiService
import com.bkplus.android.api.entity.HomeSectionEntity
import com.bkplus.android.api.entity.ItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    fun mockupData() = arrayListOf(
        HomeSectionEntity(
            id = 0,
            name = "Anime",
            data = ItemEntity.sampleListItem()
        ),
        HomeSectionEntity(
            id = 1,
            name = "Cartoon",
            data = ItemEntity.sampleListItem()
        ),
        HomeSectionEntity(
            id = 2,
            name = "Flower",
            data = ItemEntity.sampleListItem()
        ),
        HomeSectionEntity(
            id = 3,
            name = "Funny",
            data = ItemEntity.sampleListItem()
        ),
    )
}
