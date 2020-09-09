package com.startupmoguls.mastercraft.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.startupmoguls.mastercraft.data.dao.FavouritesDao
import com.startupmoguls.mastercraft.data.dao.MapsDao
import com.startupmoguls.mastercraft.data.dao.ModsDao
import com.startupmoguls.mastercraft.data.dao.SkinsDao
import com.startupmoguls.mastercraft.pojo.Favourites
import com.startupmoguls.mastercraft.pojo.Map
import com.startupmoguls.mastercraft.pojo.Mod
import com.startupmoguls.mastercraft.pojo.Skin

@Database(
    entities = [Mod::class, Map::class, Skin::class, Favourites::class],
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
    abstract fun getMapsDao(): MapsDao
    abstract fun getSkinsDao(): SkinsDao
    abstract fun getFavouritesDao(): FavouritesDao
}