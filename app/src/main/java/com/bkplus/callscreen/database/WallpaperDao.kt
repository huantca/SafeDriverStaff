package com.bkplus.callscreen.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entity: List<WallpaperEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WallpaperEntity)

    @Query("Select * From WallpaperDB where id=:string")
    fun getByName(string: String): Flow<WallpaperEntity>

    @Query("Select * From WallpaperDB order by createdTime")
    fun getAll(): Flow<List<WallpaperEntity>>

    @Query("Select * From WallpaperDB where isUsing=1")
    fun getUsing(): Flow<List<WallpaperEntity>>

    @Query("Select * From WallpaperDB where isLiked=1")
    fun getLiked(): Flow<List<WallpaperEntity>>

    @Query(
        "UPDATE WallpaperDB " +
                "SET isUsing = 0 " +
                "WHERE isUsing = 1"
    )
    suspend fun deleteUseWallpaper()

    @Query("Delete From WallpaperDB where generateId = :id")
    fun deleteItem(id: Int)

    @Query("Delete From WallpaperDB where id = :id")
    fun deleteFavourite(id: Int?)

    @Query("SELECT COUNT(generateId) FROM WallpaperDB")
    fun getCount(): Int
}
