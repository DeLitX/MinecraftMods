package com.delitx.minecraftmods.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.pojo.Skin

@Dao
interface SkinsDao {
    @Query("select * from skin order by place asc")
    fun getAll(): LiveData<List<Skin>>

    @Query("select * from skin where category=:category order by place asc")
    fun getByCategory(category: String): LiveData<List<Skin>>

    @Query("select * from skin where id=:id order by place asc")
    fun getSkin(id: String): Skin

    @Query("delete from skin")
    fun deleteAll()

    @Query("delete from skin where category=:category")
    fun deleteCategory(category: String)

    @Query("select skin.id,skin.category,skin.images,isFavourite,place from skin inner join favourites on skin.id = favourites.id order by place asc")
    fun selectFavourite(): LiveData<List<Skin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skins: List<Skin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skin: Skin)

    @Delete
    fun delete(skin: Skin)

    @Delete
    fun delete(skins: List<Skin>)
}