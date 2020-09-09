package com.startupmoguls.mastercraft.ui.mods

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.pojo.Mod
import com.startupmoguls.mastercraft.ui.BaseCategoryFragment
import com.startupmoguls.mastercraft.ui.BaseScrollFragment
import com.startupmoguls.mastercraft.ui.adapters.CategoriesAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.ModsViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory

class ModsFragment : BaseCategoryFragment<Mod>() {


    override val mViewModelClass: Class<out BaseCategoriesViewModel<Mod>> =
        ModsViewModel::class.java
    override val mCategoriesAdapter: CategoriesAdapter = CategoriesAdapter(this)
    override var mFragment: BaseScrollFragment<Mod> = ModsScrollFragment()

    override fun actionsOnStart() {
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val query = p0?.trim()
                return if (query != null && query != "") {
                    search(query)
                    true
                } else if (query == "") {
                    selectCategory(currentCategory)
                    true
                } else
                    false
            }
        })
    }

    override fun tuneViewModel() {
        (mViewModel as ModsViewModel).mCategories.observe(viewLifecycleOwner, Observer {
            mCategoriesAdapter.setList(it)
        })
    }

    override fun setupViewModel() {
        val viewModelFactory = ViewModelsFactory(
            requireActivity().application, Repository.getInstance(
                DataBase.get(requireActivity().application), requireActivity().application
            )
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ModsViewModel::class.java)
    }


}