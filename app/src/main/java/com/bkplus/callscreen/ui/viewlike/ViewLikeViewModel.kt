package com.bkplus.callscreen.ui.viewlike

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.entity.Item
import com.bkplus.callscreen.common.BasePrefers
import com.bkplus.callscreen.database.FavoriteDao
import com.bkplus.callscreen.database.FavoriteEntity
import com.bkplus.callscreen.database.WallpaperDao
import com.bkplus.callscreen.database.WallpaperEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewLikeViewModel @Inject constructor(
    private val wallpaperDao: WallpaperDao,
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    val list = MutableLiveData<List<WallPaper>>()

    fun matchWallpaperToDB(items: ArrayList<WallPaper>) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteDao.getLiked().collect { listLiked ->
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
            val favoriteEntity = FavoriteEntity()
            if (item?.url != null) {
                favoriteEntity.imageUrl = item.url
            }
            favoriteEntity.isLiked = true
            favoriteEntity.id = item?.id.toString()
            favoriteEntity.free = item?.free
            favoriteEntity.generateId = favoriteEntity.hashCode()
            favoriteEntity.loves = item?.likeCount
            favoriteEntity.createdTime = System.currentTimeMillis()
            favoriteDao.insert(favoriteEntity)
        }
    }

    fun disperse(item: WallPaper?) {
        favoriteDao.deleteFavourite(item?.id)
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
                        free = item.free,
                        loves = item.likeCount
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

