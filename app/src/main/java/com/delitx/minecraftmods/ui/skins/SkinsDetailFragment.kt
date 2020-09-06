package com.delitx.minecraftmods.ui.skins

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.pojo.Skin
import com.delitx.minecraftmods.ui.BaseDetailFragment
import com.delitx.minecraftmods.ui.adapters.ImagesViewPagerAdapter
import com.delitx.minecraftmods.viewmodels.SkinsViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory
import kotlinx.android.synthetic.main.mod_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SkinsDetailFragment : BaseDetailFragment<Skin>() {
    override val layoutId: Int = R.layout.skin_layout

    override fun setupViewModel() {
        val viewModelFactory = ViewModelsFactory(
            requireActivity().application, Repository.getInstance(
                DataBase.get(requireActivity().application)
                ,requireActivity().application)
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SkinsViewModel::class.java)
    }


    override fun bindActivity() {
        mViewPager = requireView().findViewById(R.id.images_pager)
        requireView().tab_layout.setupWithViewPager(mViewPager)
    }

    override fun setupFragmentUpdates(id: String) {
        CoroutineScope(IO).launch {
            val skin = (mViewModel as SkinsViewModel).getItem(id)
            mItem=skin
            withContext(Main) {
                setupView(skin)
            }
        }
    }

    override fun setupView(item: Skin) {
        if((mViewModel as SkinsViewModel).isSkinDownloaded(item)){
            switchToInstall()
        }
        mPagerAdapter = ImagesViewPagerAdapter(this, item.images,R.layout.rounded_image_square)
        mViewPager.adapter=mPagerAdapter
        requireView().back.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override lateinit var mItem: Skin

    override fun installItem(item: Skin) {
        (mViewModel as SkinsViewModel).install(item)
    }

    override fun downloadItem(item: Skin) {
        (mViewModel as SkinsViewModel).downloadSkin(item){switchToInstall()}
    }


}