package com.startupmoguls.mastercraft.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.ui.BaseFragment
import com.startupmoguls.mastercraft.ui.adapters.WelcomeHeaderSwipeAdapter
import kotlinx.android.synthetic.main.welcome_screen3.*

class WelcomeFragment3() : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.welcome_screen3, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindFragment()
    }

    private fun bindFragment() {
        header.adapter = WelcomeHeaderSwipeAdapter(
            mutableListOf(
                resources.getString(R.string.start_playing1),
                resources.getString(R.string.start_playing2),
                resources.getString(R.string.start_playing3)
            )
        )
    }
}