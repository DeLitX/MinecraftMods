package com.delitx.minecraftmods.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.pojo.Mod
import com.delitx.minecraftmods.pojo.Skin

@Dao
interface ModsDao {
    @Query("select * from mod order by place asc")
    fun getAll(): LiveData<List<Mod>>

    @Query("select * from mod where category=:category order by place asc")
    fun getByCategory(category: String): LiveData<List<Mod>>

    @Query("select * from mod where id=:modId order by place asc")
    fun getMod(modId: String): Mod

    @Query("delete from mod")
    fun deleteAll()

    @Query("delete from mod where category=:category")
    fun deleteCategory(category: String)

    @Query("select mod.id,mod.category,mod.images,mod.name,mod.description,mod.isFavourite,mod.place from mod inner join favourites on mod.id = favourites.id  order by place asc")
    fun selectFavourite(): LiveData<List<Mod>>

    @Query("select * from mod where description like '%'||:query||'%' or name like '%'||:query||'%' order by place asc")
    fun search(query: String): LiveData<List<Mod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mods: List<Mod>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mod: Mod)

    @Delete
    fun delete(mod: Mod)

    @Delete
    fun delete(mods: List<Mod>)
}