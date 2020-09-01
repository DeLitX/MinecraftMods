package com.delitx.minecraftmods.ui.maps

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.adapters.ItemsAdapter
import com.delitx.minecraftmods.viewmodels.BaseCategoriesViewModel
import com.delitx.minecraftmods.viewmodels.MapsViewModel

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