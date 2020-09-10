package com.startupmoguls.mastercraft.viewmodels

import androidx.lifecycle.MutableLiveData
import com.startupmoguls.mastercraft.Repository

class FirstScreenViewModel(private val mRepository: Repository):BaseViewModel(mRepository) {
    val progressState:MutableLiveData<Int> =mRepository.progressLiveData
    fun increaseProgressState(){
        mRepository.increaseProgressState()

    }
    fun resetProgressState(){
        mRepository.resetProgressState()
    }
}