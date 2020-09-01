package com.delitx.minecraftmods.viewmodels

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.delitx.minecraftmods.Repository

abstract class BaseViewModel(private val mRepository: Repository): ViewModel() {
    fun downloadPhoto(link:String,imageHolder:ImageView){
        mRepository.downloadPhoto(link, imageHolder)
    }
}