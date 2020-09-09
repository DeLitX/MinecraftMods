package com.startupmoguls.mastercraft.ui.maps

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.ui.BaseScrollFragment
import com.startupmoguls.mastercraft.ui.adapters.ItemsAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.MapsViewModel
import com.startupmoguls.mastercraft.ui.maps.MapsScrollFragmentDirections
import com.startupmoguls.mastercraft.pojo.Map

class MapsScrollFragment : BaseScrollFragment<Map>() {
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Map>> =
        MapsViewModel::class.java

    override fun onItemClick(item: Map) {
        val action = MapsScrollFragmentDirections.actionMapDetail(item.id)
        Navigation.findNavController(this.requireView()).navigate(action)
    }

    override fun actionsOnStart() {

    }

    override val mItemsAdapter: ItemsAdapter<Map> = ItemsAdapter(this, R.layout.mods_item)
    override fun setupItemsRecycler() {
        mItemsRecycler.layoutManager = LinearLayoutManager(this.context)
        mItemsRecycler.adapter = mItemsAdapter
    }

    override fun search(query: String) {
        switchToLiveData((mViewModel as MapsViewModel).search(query))
    }
}