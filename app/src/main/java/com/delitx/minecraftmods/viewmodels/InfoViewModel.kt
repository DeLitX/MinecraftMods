package com.delitx.minecraftmods.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.delitx.minecraftmods.Repository

class InfoViewModel(private val mRepository: Repository) : BaseViewModel(mRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}