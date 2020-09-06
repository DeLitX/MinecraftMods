package com.delitx.minecraftmods.ui.mods

import androidx.lifecycle.ViewModelProviders
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.pojo.Mod
import com.delitx.minecraftmods.ui.BaseDetailFragment
import com.delitx.minecraftmods.ui.adapters.ImagesViewPagerAdapter
import com.delitx.minecraftmods.viewmodels.ModsViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory
import kotlinx.android.synthetic.main.mod_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModsDetailFragment : BaseDetailFragment<Mod>() {
    override val layoutId: Int = R.layout.mod_layout

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
    }

    override lateinit var mItem: Mod

    override fun installItem(item: Mod) {
        (mViewModel as ModsViewModel).install(item)
    }

    override fun downloadItem(item: Mod) {
        (mViewModel as ModsViewModel).downloadMod(item) { switchToInstall() }
    }


}