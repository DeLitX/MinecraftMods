package com.delitx.minecraftmods.ui.maps

import androidx.lifecycle.ViewModelProviders
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.ui.BaseDetailFragment
import com.delitx.minecraftmods.ui.adapters.ImagesViewPagerAdapter
import com.delitx.minecraftmods.viewmodels.MapsViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory
import kotlinx.android.synthetic.main.mod_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsDetailFragment : BaseDetailFragment<Map>() {
    override val layoutId: Int = R.layout.mod_layout
    override fun bindActivity() {
        mViewPager = requireView().findViewById(R.id.images_pager)
        requireView().tab_layout.setupWithViewPager(mViewPager)
    }

    override fun setupViewModel() {
        val viewModelFactory = ViewModelsFactory(
            requireActivity().application, Repository(
                DataBase.get(requireActivity().application)
                ,requireActivity().application)
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)
    }

    override fun setupFragmentUpdates(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val map = (mViewModel as MapsViewModel).getItem(id)
            mItem=map
            withContext(Dispatchers.Main) {
                setupView(map)
            }
        }
    }

    override fun setupView(item: Map) {
        if((mViewModel as MapsViewModel).isMapDownloaded(item)){
            switchToInstall()
        }
        mPagerAdapter = ImagesViewPagerAdapter(this, item.images,R.layout.rounded_image)
        mViewPager.adapter=mPagerAdapter
        requireView().header.text = item.name
        requireView().description.text = item.description
        requireView().back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override lateinit var mItem: Map

    override fun installItem(item: Map) {
        (mViewModel as MapsViewModel).install(item)
    }

    override fun downloadItem(item: Map) {
        (mViewModel as MapsViewModel).downloadMap(item) { switchToInstall() }
    }
}