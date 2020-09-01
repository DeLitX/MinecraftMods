package com.delitx.minecraftmods.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.ui.BaseFragment

class WelcomeFragment1:BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_screen1,container,false)
    }
}