package com.bkplus.callscreen.ui.viewlike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewLikeViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao
) : ViewModel() {

    var wpList: ArrayList<Item> = arrayListOf()
    var positionClick: Int = 0

    fun saveFavourite(item: WallPaper?) {
        viewModelScope.launch(Dispatchers.IO){
            val wallPaperEntity = WallpaperEntity()
            if (item?.url != null) {
                wallPaperEntity.imageUrl = item.url
            }
            wallPaperEntity.isLike = true
            wallPaperEntity.id = item?.id.toString()
            wallPaperEntity.loves = item?.likeCount
            wallPaperEntity.createdTime = System.currentTimeMillis()
            wallpaperDao.insert(wallPaperEntity)
        }
    }

    fun disperse(item: WallPaper?) {
        wallpaperDao.deleteFavourite(item?.id)
    }
}