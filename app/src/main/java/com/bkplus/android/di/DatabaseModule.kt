package com.bkplus.android.di

import android.content.Context
import com.bkplus.android.database.RoomDB
import com.bkplus.android.database.DbDao
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
    fun provideWallpaperDao(roomDB: RoomDB): DbDao {
        return roomDB.itemDB()
    }
}
