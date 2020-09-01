package com.delitx.minecraftmods.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.pojo.Mod

@Dao
interface MapsDao {
    @Query("select * from map order by place asc")
    fun getAll(): LiveData<List<Map>>

    @Query("select * from map where category=:category order by place asc")
    fun getByCategory(category: String): LiveData<List<Map>>

    @Query("delete from map")
    fun deleteAll()

    @Query("select * from map where id=:id order by place asc")
    fun getMap(id: String): Map

    @Query("delete from map where category=:category")
    fun deleteCategory(category: String)

    @Query("select map.id,map.category,map.images,map.name,map.description,map.isFavourite,map.place from map inner join favourites on map.id = favourites.id order by place asc")
    fun selectFavourite(): LiveData<List<Map>>

    @Query("select * from map where description like '%'||:query||'%' or name like '%'||:query||'%' order by place asc")
    fun search(query: String): LiveData<List<Map>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(maps: List<Map>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(map: Map)

    @Delete
    fun delete(map: Map)

    @Delete
    fun delete(maps: List<Map>)
}