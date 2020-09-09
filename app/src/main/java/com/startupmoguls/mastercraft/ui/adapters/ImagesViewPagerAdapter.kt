package com.startupmoguls.mastercraft.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.startupmoguls.mastercraft.ui.SquareConstraintLayout
import com.startupmoguls.mastercraft.ui.adapters.interfaces.DownloadImage
import kotlinx.android.synthetic.main.rounded_image.view.item_image

class ImagesViewPagerAdapter(
    private val mInteraction: DownloadImage,
    private val mLinks: List<String>,
    private val layoutId: Int
) : PagerAdapter() {
    override fun getCount(): Int {
        return mLinks.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(layoutId, container, false)
        val imageView = view.item_image
        mInteraction.downloadImage(mLinks[position], imageView)
        view.clipToOutline = true
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is SquareConstraintLayout)
            container.removeView(`object`)
        else if (`object` is LinearLayout)
            container.removeView(`object`)

    }
}