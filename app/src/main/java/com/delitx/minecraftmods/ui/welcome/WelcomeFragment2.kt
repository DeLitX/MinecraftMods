package com.delitx.minecraftmods.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.ui.BaseFragment
import kotlinx.android.synthetic.main.welcome_screen2.view.*

class WelcomeFragment2(private val headerText:String):BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.welcome_screen2,container,false)
        v.header.text=headerText
        return v
    }
}