package com.delitx.minecraftmods.ui.skins

import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.pojo.Mod
import com.delitx.minecraftmods.pojo.Skin
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.adapters.ItemsAdapter
import com.delitx.minecraftmods.viewmodels.BaseCategoriesViewModel
import com.delitx.minecraftmods.viewmodels.MapsViewModel
import com.delitx.minecraftmods.viewmodels.ModsViewModel
import com.delitx.minecraftmods.viewmodels.SkinsViewModel

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