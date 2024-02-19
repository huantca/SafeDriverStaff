package com.bkplus.callscreen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Objects

@Entity(tableName = "WallpaperDB")
class WallpaperEntity(
    @PrimaryKey var generateId: Int = 0,
    @ColumnInfo(name = "id") var id: String? = null,
    @ColumnInfo(name = "isUsing") var isUsing: Boolean? = false,
    @ColumnInfo(name = "isUsed") var isUsed: Boolean? = false,
    @ColumnInfo(name = "imageUrl") var imageUrl: String? = null,
    @ColumnInfo(name = "imageUri") var imageUri: String? = null,
    @ColumnInfo(name = "createdTime") var createdTime: Long = 0,
    @ColumnInfo(name = "isSelected") var isSelected: Boolean = false,
) {
    override fun hashCode(): Int {
        return Objects.hash(
            id,
            imageUrl,
            imageUri
        )
    }
}
