package com.startupmoguls.mastercraft.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.ui.BaseFragment
import kotlinx.android.synthetic.main.welcome_screen2.*
import kotlinx.android.synthetic.main.welcome_screen2.view.*

class WelcomeFragment2(private val headerText: String) : BaseFragment() {
    private var mCurrentChosen = 2
    private var mCategories: List<Pair<LinearLayout, TextView>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.welcome_screen2, container, false)
        v.header.text = headerText
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindFragment()
    }

    private fun bindFragment() {
        mCategories =
            listOf(Pair(mods1, text_mods1), Pair(mods2, text_mods2), Pair(mods3, text_mods3))
        for (i in mCategories!!.indices) {
            mCategories!![i].first.setOnClickListener {
                if (mCurrentChosen != i) {
                    unchoseItem(mCategories!![mCurrentChosen])
                    choseItem(mCategories!![i])
                    mCurrentChosen = i
                }
            }
        }
    }

    private fun choseItem(item: Pair<LinearLayout, TextView>) {
        item.first.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rounded_corners_dark, null)
        item.second.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
    }

    private fun unchoseItem(item: Pair<LinearLayout, TextView>) {
        item.first.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rounded_corners, null)
        item.second.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.colorPrimaryDark,
                null
            )
        )
    }
}