package com.startupmoguls.mastercraft.ui.mods

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.pojo.Mod
import com.startupmoguls.mastercraft.ui.BaseScrollFragment
import com.startupmoguls.mastercraft.ui.adapters.ItemsAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.ModsViewModel
import com.startupmoguls.minecraftmods.ui.mods.ModsScrollFragmentDirections

class ModsScrollFragment : BaseScrollFragment<Mod>() {
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Mod>> =
        ModsViewModel::class.java

    override fun onItemClick(item: Mod) {
        val action = ModsScrollFragmentDirections.actionModDetail(item.id)
        Navigation.findNavController(this.requireView()).navigate(action)
    }

    override fun actionsOnStart() {

    }

    override val mItemsAdapter: ItemsAdapter<Mod> = ItemsAdapter(this, R.layout.mods_item)
    override fun setupItemsRecycler() {
        mItemsRecycler.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        mItemsRecycler.adapter = mItemsAdapter
    }

    override fun search(query: String) {
        switchToLiveData((mViewModel as ModsViewModel).search(query))
    }
}