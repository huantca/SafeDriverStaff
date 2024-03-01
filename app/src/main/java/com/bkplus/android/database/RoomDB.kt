package com.bkplus.android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DatabaseEntity::class],
    version = 1
)
abstract class RoomDB : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: RoomDB? = null
        fun getInstance(context: Context): RoomDB {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(
                context,
                RoomDB::class.java,
                "WallpaperDB"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
//                .apply {
//                openHelper.writableDatabase
//            }
        }
    }

    abstract fun itemDB(): DbDao
}
