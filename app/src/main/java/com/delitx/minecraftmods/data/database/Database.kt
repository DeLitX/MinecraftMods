package com.delitx.minecraftmods.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delitx.minecraftmods.data.dao.*
import com.delitx.minecraftmods.pojo.*
import com.delitx.minecraftmods.pojo.Map

@Database(
    entities = [Mod::class, Map::class, Skin::class,Favourites::class],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    companion object {
        private var mInstance: DataBase? = null
        fun get(application: Application): DataBase {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(
                    application,
                    DataBase::class.java, "database"
                ).fallbackToDestructiveMigration().build()
            }
            return mInstance!!
        }
    }

    abstract fun getModsDao(): ModsDao
    abstract fun getMapsDao():MapsDao
    abstract fun getSkinsDao():SkinsDao
    abstract fun getFavouritesDao():FavouritesDao
}