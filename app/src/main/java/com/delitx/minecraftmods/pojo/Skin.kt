package com.delitx.minecraftmods.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.delitx.minecraftmods.data.converters.LinksListConverter

@Entity
@TypeConverters(LinksListConverter::class)
data class Skin(
    @PrimaryKey
    override var id: String,
    override var images: List<String> = listOf(),
    override var isFavourite: Boolean = false,
    override var category: String = "",
    override var place:Int=0
) : BaseClass()