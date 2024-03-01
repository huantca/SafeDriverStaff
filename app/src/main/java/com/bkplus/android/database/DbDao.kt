package com.bkplus.android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entity: List<DatabaseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DatabaseEntity)

    @Query("Select * From AppDatabase where id=:string")
    fun getByName(string: String): Flow<DatabaseEntity>

    @Query("Select * From AppDatabase order by createdTime")
    fun getAll(): Flow<List<DatabaseEntity>>


    @Query("Select * From AppDatabase where isLiked=1")
    fun getLiked(): Flow<List<DatabaseEntity>>

    @Query("Delete From AppDatabase where generateId = :id")
    fun deleteItem(id: Int)

    @Query("SELECT COUNT(generateId) FROM AppDatabase")
    fun getCount(): Int

    @Query("SELECT COUNT(generateId) FROM AppDatabase where isLiked = 1")
    fun getLikeCount(): Int
}
