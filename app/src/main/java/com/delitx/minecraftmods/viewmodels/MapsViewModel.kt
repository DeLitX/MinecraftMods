package com.delitx.minecraftmods.viewmodels

import androidx.lifecycle.LiveData
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.pojo.Map
import javax.crypto.ExemptionMechanismSpi

class MapsViewModel(private val mRepository: Repository) : BaseCategoriesViewModel<Map>(mRepository) {
    val mCategories=mRepository.mapCategories

    override suspend fun getItem(id: String): Map {
        return mRepository.getMap(id)
    }

    override fun getItems(category: String): LiveData<List<Map>> {
        return mRepository.getMaps(category)
    }
    fun downloadMap(map:Map,actionAfter:()->Unit){
        mRepository.downloadMap(map,actionAfter)
    }
    fun install(map: Map){
        mRepository.openMap(map)
    }
    fun isMapDownloaded(map: Map):Boolean{
        return mRepository.checkIfMapDownloaded(map)
    }
    fun search(query:String):LiveData<List<Map>>{
        return mRepository.searchMap(query)
    }

    override fun likeItem(item: Map) {
        mRepository.likeMap(item)
    }

    override fun dislikeItem(item: Map) {
        mRepository.dislikeMap(item)
    }

    override fun getFavourites(): LiveData<List<Map>> {
        return mRepository.getMapFavourites()
    }

    override fun refreshCategories() {
        mRepository.refreshMapCategories()
    }

}