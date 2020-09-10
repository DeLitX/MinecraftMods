package com.startupmoguls.mastercraft.ui.mods

import androidx.lifecycle.ViewModelProviders
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.pojo.Mod
import com.startupmoguls.mastercraft.ui.BaseDetailFragment
import com.startupmoguls.mastercraft.ui.adapters.ImagesViewPagerAdapter
import com.startupmoguls.mastercraft.viewmodels.MapsViewModel
import com.startupmoguls.mastercraft.viewmodels.ModsViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.mod_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModsDetailFragment : BaseDetailFragment<Mod>() {
    override val layoutId: Int = com.startupmoguls.mastercraft.R.layout.mod_layout

    override fun setupViewModel() {
        val viewModelFactory = ViewModelsFactory(
            requireActivity().application, Repository.getInstance(
                DataBase.get(requireActivity().application), requireActivity().application
            )
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ModsViewModel::class.java)
    }


    override fun bindActivity() {
        mViewPager = requireView().findViewById(R.id.images_pager)
        requireView().tab_layout.setupWithViewPager(mViewPager)
    }

    override fun setupFragmentUpdates(id: String) {
        CoroutineScope(IO).launch {
            val mod = (mViewModel as ModsViewModel).getItem(id)
            mItem=mod
            withContext(Main) {
                setupView(mod)
            }
        }
    }

    override fun setupView(item: Mod) {
        if ((mViewModel as ModsViewModel).isModDownloaded(item)){
            switchToInstall()
        }
        mPagerAdapter = ImagesViewPagerAdapter(this, item.images, R.layout.rounded_image)
        mViewPager.adapter=mPagerAdapter
        requireView().header.text = item.name
        requireView().description.text = item.description
        requireView().back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val isLiked: CircleImageView = requireView().findViewById(R.id.liked)
        isLiked.setImageResource(if (item.isFavourite) R.drawable.ic_favorite_liked else R.drawable.ic_favorite_not_liked)
        isLiked.setOnClickListener {
            if (item.isFavourite) {
                isLiked.setImageResource(R.drawable.ic_favorite_not_liked)
                (mViewModel as ModsViewModel).dislikeItem(item)
                item.isFavourite=false
            } else {
                isLiked.setImageResource(R.drawable.ic_favorite_liked)
                (mViewModel as ModsViewModel).likeItem(item)
                item.isFavourite=true
            }
        }
    }

    override lateinit var mItem: Mod

    override fun installItem(item: Mod) {
        (mViewModel as ModsViewModel).install(item)
    }

    override fun downloadItem(item: Mod) {
        (mViewModel as ModsViewModel).downloadMod(item) { switchToInstall() }
    }


}