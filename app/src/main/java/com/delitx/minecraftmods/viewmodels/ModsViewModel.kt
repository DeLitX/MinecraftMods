package com.delitx.minecraftmods.viewmodels

import androidx.lifecycle.LiveData
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.pojo.Category
import com.delitx.minecraftmods.pojo.Mod

class ModsViewModel(private val mRepository: Repository) :
    BaseCategoriesViewModel<Mod>(mRepository) {
    val mCategories:LiveData<List<Category>>
    get() = mRepository.modCategories

    override suspend fun getItem(id: String): Mod {
        return mRepository.getMod(id)
    }

    override fun getItems(category: String): LiveData<List<Mod>> {
        return mRepository.getMods(category)
    }

    fun downloadMod(mod: Mod, actionAfter: () -> Unit) {
        mRepository.downloadMod(mod, actionAfter)
    }

    fun install(mod: Mod) {
        mRepository.openMod(mod)
    }

    fun isModDownloaded(mod: Mod): Boolean {
        return mRepository.checkIfModDownloaded(mod)
    }

    fun search(query: String): LiveData<List<Mod>> {
        return mRepository.searchMod(query)
    }

    override fun likeItem(item: Mod) {
        mRepository.likeMod(item)
    }

    override fun dislikeItem(item: Mod) {
        mRepository.dislikeMod(item)
    }

    override fun getFavourites(): LiveData<List<Mod>> {
        return mRepository.getModFavourites()
    }
}