package com.startupmoguls.mastercraft.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.ui.BaseFragment
import kotlinx.android.synthetic.main.welcome_screen2.*
import kotlinx.android.synthetic.main.welcome_screen2.view.*

class WelcomeFragment2(private val headerText: String) : BaseFragment() {
    private var mCategories: List<ButtonItem>? = null
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
            mutableListOf(
                ButtonItem(mods1, text_mods1, false),
                ButtonItem(mods2, text_mods2, false),
                ButtonItem(mods3, text_mods3, true)
            )
        for (i in mCategories!!.indices) {
            mCategories!![i].root.setOnClickListener {
                if (mCategories!![i].isEnabled) {
                    unchoseItem(mCategories!![i])
                } else {
                    choseItem(mCategories!![i])
                }
            }
        }
    }

    private fun choseItem(item: ButtonItem) {
        item.root.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rounded_corners_dark, null)
        item.text.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        item.isEnabled = true
    }

    private fun unchoseItem(item: ButtonItem) {
        item.root.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rounded_corners, null)
        item.text.setTextColor(
            ResourcesCompat.getColor(
                resources,
                R.color.colorPrimaryDark,
                null
            )
        )
        item.isEnabled=false
    }

    data class ButtonItem(
        var root: ConstraintLayout,
        var text: TextView,
        var isEnabled: Boolean
    )
}