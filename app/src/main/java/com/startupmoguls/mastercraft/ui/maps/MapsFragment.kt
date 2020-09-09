package com.startupmoguls.mastercraft.ui.maps

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.ui.BaseCategoryFragment
import com.startupmoguls.mastercraft.ui.BaseScrollFragment
import com.startupmoguls.mastercraft.ui.adapters.CategoriesAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.MapsViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory
import com.startupmoguls.mastercraft.pojo.Map

class MapsFragment : BaseCategoryFragment<Map>() {
    override val mCategoriesAdapter: CategoriesAdapter = CategoriesAdapter(this)
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Map>> =
        MapsViewModel::class.java
    override var mFragment: BaseScrollFragment<Map> = MapsScrollFragment()


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
            requireActivity().application, Repository.getInstance(
                DataBase.get(requireActivity().application), requireActivity().application
            )
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)
    }

}