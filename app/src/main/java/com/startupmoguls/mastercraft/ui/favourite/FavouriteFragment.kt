package com.startupmoguls.mastercraft.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.ui.BaseFragment
import com.startupmoguls.mastercraft.ui.adapters.FragmentFavouritesAdapter
import com.startupmoguls.mastercraft.ui.maps.MapsScrollFragment
import com.startupmoguls.mastercraft.ui.mods.ModsScrollFragment
import com.startupmoguls.mastercraft.ui.skins.SkinsScrollFragment
import com.startupmoguls.mastercraft.viewmodels.FavouriteViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory
import kotlinx.android.synthetic.main.fragment_favourite.view.*

class FavouriteFragment : BaseFragment() {

    private lateinit var mFavouriteViewModel: FavouriteViewModel
    private lateinit var mTabs: TabLayout
    private lateinit var mPager: ViewPager2
    private lateinit var mAdapter: FragmentFavouritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = ViewModelsFactory(
            this.requireActivity().application, Repository.getInstance(
                DataBase.get(this.requireActivity().application), requireActivity().application
            )
        )
        mFavouriteViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favourite, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindFragment()
    }

    private fun bindFragment() {
        mTabs = requireView().tabs
        mPager = requireView().pager
        mAdapter = FragmentFavouritesAdapter(
            listOf(
                MapsScrollFragment(),
                ModsScrollFragment(),
                SkinsScrollFragment()
            ), this
        ) {
            it.selectFavourites()
            it.marginHorizontal()
        }
        mPager.adapter = mAdapter
        TabLayoutMediator(mTabs, mPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = resources.getString(
                when (position) {
                    0 -> R.string.maps
                    1 -> R.string.mods
                    else -> R.string.skins
                }
            )
        }.attach()
    }
}