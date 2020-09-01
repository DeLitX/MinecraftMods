package com.delitx.minecraftmods.ui.maps

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.ui.BaseCategoryFragment
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.adapters.CategoriesAdapter
import com.delitx.minecraftmods.viewmodels.BaseCategoriesViewModel
import com.delitx.minecraftmods.viewmodels.MapsViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory

class MapsFragment :BaseCategoryFragment<Map>() {
    override val mCategoriesAdapter: CategoriesAdapter = CategoriesAdapter(this)
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Map>> =
        MapsViewModel::class.java
    override var mFragment: BaseScrollFragment<Map> =MapsScrollFragment()


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
        (mViewModel as MapsViewModel).mCategories.observe(viewLifecycleOwner) {
            mCategoriesAdapter.setList(it)
        }
    }

    override fun setupViewModel() {
        val viewModelFactory = ViewModelsFactory(
            requireActivity().application, Repository(
                DataBase.get(requireActivity().application), requireActivity().application
            )
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)
    }

}