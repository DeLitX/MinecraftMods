package com.startupmoguls.mastercraft.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourites(
    @PrimaryKey
    var id:String
)