package com.delitx.minecraftmods.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.delitx.minecraftmods.data.converters.LinksListConverter

@Entity
@TypeConverters(LinksListConverter::class)
data class Mod(
    @PrimaryKey
    override var id: String = "",
    override var category: String = "",
    override var isFavourite: Boolean = false,
    override var images: List<String> = listOf(),
    var name: String = "",
    var description: String = "",
    override var place:Int=0
) : BaseClass()