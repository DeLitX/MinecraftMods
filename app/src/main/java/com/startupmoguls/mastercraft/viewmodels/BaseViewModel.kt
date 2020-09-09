package com.startupmoguls.mastercraft.viewmodels

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.startupmoguls.mastercraft.Repository

abstract class BaseViewModel(private val mRepository: Repository): ViewModel() {
    fun downloadPhoto(link:String,imageHolder:ImageView){
        mRepository.downloadPhoto(link, imageHolder)
    }
}