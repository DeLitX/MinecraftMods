package com.delitx.minecraftmods.ui.mods

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.pojo.Mod
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.adapters.ItemsAdapter
import com.delitx.minecraftmods.viewmodels.BaseCategoriesViewModel
import com.delitx.minecraftmods.viewmodels.MapsViewModel
import com.delitx.minecraftmods.viewmodels.ModsViewModel

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