package com.startupmoguls.mastercraft.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.startupmoguls.mastercraft.pojo.BaseClass
import com.startupmoguls.mastercraft.ui.BaseScrollFragment

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