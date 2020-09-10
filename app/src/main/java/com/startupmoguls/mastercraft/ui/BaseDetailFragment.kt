package com.startupmoguls.mastercraft.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.ui.adapters.ImagesViewPagerAdapter
import com.startupmoguls.mastercraft.ui.adapters.interfaces.DownloadImage
import com.startupmoguls.mastercraft.viewmodels.BaseViewModel
import com.startupmoguls.mastercraft.ui.mods.ModsDetailFragmentArgs
import kotlinx.android.synthetic.main.button_activated.*
import kotlinx.android.synthetic.main.button_activated.view.*
import kotlinx.android.synthetic.main.button_not_activated.view.*
import kotlinx.android.synthetic.main.mod_layout.*

abstract class BaseDetailFragment<T> : BaseFragment(), DownloadImage {
    internal abstract val layoutId: Int
    internal lateinit var mViewModel: BaseViewModel
    internal lateinit var mViewPager: ViewPager
    internal lateinit var mPagerAdapter: ImagesViewPagerAdapter
    internal abstract var mItem: T

    internal abstract fun bindActivity()
    internal abstract fun setupViewModel()
    internal abstract fun setupFragmentUpdates(id: String)
    internal abstract fun setupView(item: T)
    internal abstract fun installItem(item: T)
    internal abstract fun downloadItem(item: T)

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

    internal fun switchToDownloading() {
        requireView().button.visibility = View.GONE
        val downloadingButton = requireView().button_layout
        downloadingButton.visibility = View.VISIBLE
        downloadingButton.isEnabled = false
    }

    internal fun switchToInstall() {
        requireView().button_layout.visibility = View.GONE
        val installButton = requireView().button
        installButton.visibility = View.VISIBLE
        installButton.text = resources.getString(R.string.install)
        installButton.setOnClickListener {
            installItem(mItem)
        }
        installButton.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                install_active.visibility = View.VISIBLE
                installButton.visibility = View.GONE
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                install_active.visibility = View.GONE
                installButton.visibility = View.VISIBLE
                view.performClick()
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindActivity()
    }

    override fun downloadImage(path: String, imageHolder: ImageView) {
        mViewModel.downloadPhoto(path, imageHolder)
    }
}