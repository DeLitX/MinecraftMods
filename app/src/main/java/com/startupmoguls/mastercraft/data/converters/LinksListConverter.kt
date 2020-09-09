package com.startupmoguls.mastercraft.data.converters

import androidx.room.TypeConverter

class LinksListConverter {
    @TypeConverter
    fun toString(links: List<String>): String {
        var isFirst = true
        var result = ""
        for (i in links) {
            if (!isFirst) {
                result += " "
            }
            result += i
            isFirst = false
        }
        return result
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        return string.split(" ")
    }
}