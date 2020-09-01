package com.delitx.minecraftmods.data.dao

import androidx.room.*
import com.delitx.minecraftmods.pojo.Favourites

@Dao
interface FavouritesDao {
    @Query("select * from favourites")
    fun selectAll():List<Favourites>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item:Favourites)
    @Delete
    fun delete(item:Favourites)
}