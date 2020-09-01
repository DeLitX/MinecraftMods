package com.delitx.minecraftmods.network

import android.widget.ImageView
import com.google.firebase.firestore.DocumentSnapshot
import java.io.File

interface FirebaseRequests {
    fun getJSONs()
    suspend fun loadFile(link: String, saveTo: File): Boolean
    fun loadImage(link: String, imageHolder: ImageView)
}