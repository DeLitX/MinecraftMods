package com.delitx.minecraftmods.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.viewmodels.InfoViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    private lateinit var mInfoViewModel: InfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = ViewModelsFactory(
            this.requireActivity().application, Repository.getInstance(
                DataBase.get(this.requireActivity().application)
                ,requireActivity().application)
        )
        mInfoViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(InfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_info, container, false)
        root.minecraft.clipToOutline=true
        root.import_image.clipToOutline=true
        root.map_list.clipToOutline=true
        root.resource_packs.clipToOutline=true
        root.enjoy.clipToOutline=true
        return root
    }
}