package com.delitx.minecraftmods.viewmodels

import androidx.lifecycle.LiveData
import com.delitx.minecraftmods.Repository

abstract class BaseCategoriesViewModel<T>(private val mRepository: Repository) :
    BaseViewModel(mRepository) {
    abstract suspend fun getItem(id: String): T
    abstract fun getItems(category: String = ""): LiveData<List<T>>
    abstract fun likeItem(item:T)
    abstract fun dislikeItem(item:T)
    abstract fun getFavourites():LiveData<List<T>>
    abstract fun refreshCategories()
}