package com.delitx.minecraftmods.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.ui.adapters.ImagesViewPagerAdapter
import com.delitx.minecraftmods.ui.adapters.interfaces.DownloadImage
import com.delitx.minecraftmods.ui.mods.ModsDetailFragmentArgs
import com.delitx.minecraftmods.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.button_activated.view.*
import kotlinx.android.synthetic.main.button_not_activated.view.*

abstract class BaseDetailFragment<T> : BaseFragment(), DownloadImage {
    internal abstract val layoutId: Int
    internal lateinit var mViewModel: BaseViewModel
    internal lateinit var mViewPager: ViewPager
    internal lateinit var mPagerAdapter: ImagesViewPagerAdapter
    internal abstract var mItem:T

    internal abstract fun bindActivity()
    internal abstract fun setupViewModel()
    internal abstract fun setupFragmentUpdates(id: String)
    internal abstract fun setupView(item:T)
    internal abstract fun installItem(item:T)
    internal abstract fun downloadItem(item:T)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(layoutId, container, false)
        setupViewModel()
        arguments?.let {
            val safeArgs = ModsDetailFragmentArgs.fromBundle(it)
            setupFragmentUpdates(safeArgs.id)
        }
        root.button.setOnClickListener {
            switchToDownloading()
            downloadItem(mItem)
        }
        return root
    }
    internal fun switchToDownloading(){
        requireView().button.visibility=View.GONE
        val downloadingButton=requireView().button_layout
        downloadingButton.visibility=View.VISIBLE
        downloadingButton.isEnabled=false
    }
    internal fun switchToInstall(){
        requireView().button_layout.visibility=View.GONE
        val installButton=requireView().button
        installButton.visibility=View.VISIBLE
        installButton.text=resources.getString(R.string.install)
        installButton.setOnClickListener {
            installItem(mItem)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindActivity()
    }

    override fun downloadImage(path: String, imageHolder: ImageView) {
        mViewModel.downloadPhoto(path, imageHolder)
    }
}