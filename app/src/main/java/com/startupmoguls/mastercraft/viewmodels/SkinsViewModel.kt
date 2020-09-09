package com.startupmoguls.mastercraft.viewmodels

import androidx.lifecycle.LiveData
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.pojo.Category
import com.startupmoguls.mastercraft.pojo.Skin

class SkinsViewModel(private val mRepository: Repository) : BaseCategoriesViewModel<Skin>(mRepository) {
    val mCategories:LiveData<List<Category>>
    init {
        mCategories=mRepository.skinCategories
    }

    override suspend fun getItem(id: String): Skin {
        return mRepository.getSkin(id)
    }

    override fun getItems(category: String): LiveData<List<Skin>> {
        return mRepository.getSkins(category)
    }
    fun downloadSkin(skin: Skin, actionAfter:()->Unit){
        mRepository.downloadSkin(skin,actionAfter)
    }
    fun install(skin:Skin){
        mRepository.installSkin(skin)
    }
    fun isSkinDownloaded(skin:Skin):Boolean{
        return mRepository.checkIfSkinDownloaded(skin)
    }

    override fun likeItem(item: Skin) {
        mRepository.likeSkin(item)
    }

    override fun dislikeItem(item: Skin) {
        mRepository.dislikeSkin(item)
    }

    override fun getFavourites(): LiveData<List<Skin>> {
        return mRepository.getSkinFavourites()
    }

    override fun refreshCategories() {
        mRepository.refreshSkinCategories()
    }
}