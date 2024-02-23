package com.bkplus.callscreen.di

import android.content.Context
import com.bkplus.callscreen.database.FavoriteDao
import com.bkplus.callscreen.database.RoomDB
import com.bkplus.callscreen.database.WallpaperDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): RoomDB {
        return RoomDB.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideWallpaperDao(roomDB: RoomDB): WallpaperDao {
        return roomDB.itemDB()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(roomDB: RoomDB): FavoriteDao {
        return roomDB.favoriteDB()
    }
}
