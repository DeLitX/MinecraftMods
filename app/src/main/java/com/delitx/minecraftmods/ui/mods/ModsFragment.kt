package com.delitx.minecraftmods.ui.mods

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.pojo.Mod
import com.delitx.minecraftmods.ui.BaseCategoryFragment
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.adapters.CategoriesAdapter
import com.delitx.minecraftmods.viewmodels.BaseCategoriesViewModel
import com.delitx.minecraftmods.viewmodels.ModsViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory

class ModsFragment : BaseCategoryFragment<Mod>() {


    override val mViewModelClass: Class<out BaseCategoriesViewModel<Mod>> =
        ModsViewModel::class.java
    override val mCategoriesAdapter: CategoriesAdapter = CategoriesAdapter(this)
    override var mFragment: BaseScrollFragment<Mod> =ModsScrollFragment()

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
            requireActivity().application, Repository(
                DataBase.get(requireActivity().application), requireActivity().application
            )
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ModsViewModel::class.java)
    }


}