package com.startupmoguls.mastercraft.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.startupmoguls.mastercraft.Repository

class FavouriteViewModel(private val mRepository: Repository) : BaseViewModel(mRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}