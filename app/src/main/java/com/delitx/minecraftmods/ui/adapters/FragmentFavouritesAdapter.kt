package com.delitx.minecraftmods.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.delitx.minecraftmods.pojo.BaseClass
import com.delitx.minecraftmods.pojo.Skin
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.maps.MapsScrollFragment
import com.delitx.minecraftmods.ui.mods.ModsScrollFragment
import com.delitx.minecraftmods.ui.skins.SkinsScrollFragment

class FragmentFavouritesAdapter(
    private val mList: List<BaseScrollFragment<out BaseClass>>,
    private val fragment: Fragment,
    private val onFragmentInitialisation: (BaseScrollFragment<out BaseClass>) -> Unit
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = mList[position]
        onFragmentInitialisation(fragment)
        return fragment
    }
}