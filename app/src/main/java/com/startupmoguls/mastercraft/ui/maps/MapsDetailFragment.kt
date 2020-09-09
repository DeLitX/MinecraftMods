package com.startupmoguls.mastercraft.ui.maps

import androidx.lifecycle.ViewModelProviders
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.ui.BaseDetailFragment
import com.startupmoguls.mastercraft.ui.adapters.ImagesViewPagerAdapter
import com.startupmoguls.mastercraft.viewmodels.MapsViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory
import com.startupmoguls.mastercraft.pojo.Map
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
            requireActivity().application, Repository.getInstance(
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