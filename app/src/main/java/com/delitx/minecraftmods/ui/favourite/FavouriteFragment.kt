package com.delitx.minecraftmods.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.ui.BaseFragment
import com.delitx.minecraftmods.ui.adapters.FragmentFavouritesAdapter
import com.delitx.minecraftmods.ui.maps.MapsScrollFragment
import com.delitx.minecraftmods.ui.mods.ModsScrollFragment
import com.delitx.minecraftmods.ui.skins.SkinsScrollFragment
import com.delitx.minecraftmods.viewmodels.FavouriteViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
            this.requireActivity().application, Repository(
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
        ) { it.selectFavourites() }
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