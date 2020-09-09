package com.startupmoguls.mastercraft.network

import android.widget.ImageView
import java.io.File

interface FirebaseRequests {
    fun getJSONs()
    suspend fun loadFile(link: String, saveTo: File): Boolean
    fun loadImage(link: String, imageHolder: ImageView)
}