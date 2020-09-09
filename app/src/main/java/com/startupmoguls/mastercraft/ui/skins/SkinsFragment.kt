package com.startupmoguls.mastercraft.ui.skins

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.pojo.Skin
import com.startupmoguls.mastercraft.ui.BaseCategoryFragment
import com.startupmoguls.mastercraft.ui.BaseScrollFragment
import com.startupmoguls.mastercraft.ui.adapters.CategoriesAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.SkinsViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory

class SkinsFragment : BaseCategoryFragment<Skin>() {
    override val mCategoriesAdapter: CategoriesAdapter = CategoriesAdapter(this)
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Skin>> =
        SkinsViewModel::class.java
    override var mFragment: BaseScrollFragment<Skin> = SkinsScrollFragment()
    override fun actionsOnStart() {
        mSearchView.visibility= View.GONE
    }

    override fun tuneViewModel() {
        (mViewModel as SkinsViewModel).mCategories.observe(this) {
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
            ViewModelProviders.of(this, viewModelFactory).get(SkinsViewModel::class.java)
    }

}