package com.startupmoguls.mastercraft.pojo

import androidx.room.*
import com.startupmoguls.mastercraft.data.converters.LinksListConverter

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