package com.bkplus.android.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Objects

@Entity(tableName = "AppDatabase")
class DatabaseEntity(
    @PrimaryKey var generateId: Int = 0,
    @ColumnInfo(name = "id") var id: String? = null,
    @ColumnInfo(name = "imageUrl") var imageUrl: String? = null,
    @ColumnInfo(name = "imageUri") var imageUri: String? = null,
    @ColumnInfo(name = "createdTime") var createdTime: Long = 0,
    @ColumnInfo(name = "isLiked") var isLiked: Boolean = false,
    @ColumnInfo(name = "free") var free: Boolean? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(
            id,
            imageUrl,
            imageUri
        )
    }
}