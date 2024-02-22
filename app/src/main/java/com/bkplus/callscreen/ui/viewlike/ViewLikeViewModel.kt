package com.bkplus.callscreen.ui.viewlike

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
import javax.inject.Inject

@HiltViewModel
class ViewLikeViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao
) : ViewModel() {

    val list = MutableLiveData<List<WallPaper>>()

    fun matchWallpaperToDB(items: ArrayList<WallPaper>) {
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDao.getLiked().collect { listLiked ->
                listLiked.forEach { item ->
                    items.find {
                        it.url == item.imageUrl
                    }?.apply {
                        likeCount = likeCount?.plus(1)
                        isLiked = true
                    }
                }
            }
        }
    }

    fun saveFavourite(item: WallPaper?) {
        viewModelScope.launch(Dispatchers.IO) {
            val wallPaperEntity = WallpaperEntity()
            if (item?.url != null) {
                wallPaperEntity.imageUrl = item.url
            }
            wallPaperEntity.isLiked = true
            wallPaperEntity.id = item?.id.toString()
            wallPaperEntity.free = item?.free
            wallPaperEntity.generateId = wallPaperEntity.hashCode()
            wallPaperEntity.loves = item?.likeCount
            wallPaperEntity.createdTime = System.currentTimeMillis()
            wallpaperDao.insert(wallPaperEntity)
        }
    }

    fun disperse(item: WallPaper?) {
        wallpaperDao.deleteFavourite(item?.id)
    }

    fun saveHistory(item: WallPaper?) {
        if (item != null) {
            viewModelScope.launch(Dispatchers.IO) {
                wallpaperDao.insert(
                    WallpaperEntity(
                        id = item.id.toString(),
                        isUsed = true,
                        isUsing = true,
                        imageUrl = item.url,
                    ).apply {
                        generateId = this.hashCode()
                    }
                )
            }
        }
    }

    fun freeItem(item: Item) {
        val freeItems = BasePrefers.getPrefsInstance().listItemsFree
        freeItems.add(item)
        BasePrefers.getPrefsInstance().listItemsFree = freeItems
    }
}

