package com.startupmoguls.mastercraft.data.dao

import androidx.room.*
import com.startupmoguls.mastercraft.pojo.Favourites

@Dao
interface FavouritesDao {
    @Query("select * from favourites")
    fun selectAll():List<Favourites>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item:Favourites)
    @Delete
    fun delete(item:Favourites)
}