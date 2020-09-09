package com.startupmoguls.mastercraft.ui.skins

import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.pojo.Skin
import com.startupmoguls.mastercraft.ui.BaseScrollFragment
import com.startupmoguls.mastercraft.ui.adapters.ItemsAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.SkinsViewModel
import com.startupmoguls.mastercraft.ui.skins.SkinsScrollFragmentDirections

class SkinsScrollFragment : BaseScrollFragment<Skin>() {
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Skin>> =
        SkinsViewModel::class.java

    override fun onItemClick(item: Skin) {
        val action = SkinsScrollFragmentDirections.actionSkinDetail(item.id)
        Navigation.findNavController(this.requireView()).navigate(action)
    }

    override fun actionsOnStart() {

    }

    override val mItemsAdapter: ItemsAdapter<Skin> = ItemsAdapter(this, R.layout.skin_item)
    override fun setupItemsRecycler() {
        mItemsRecycler.layoutManager = GridLayoutManager(this.context,2)
        mItemsRecycler.adapter = mItemsAdapter
    }

    override fun search(query: String) {
    }

}