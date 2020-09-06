package com.delitx.minecraftmods.ui.skins

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.pojo.Skin
import com.delitx.minecraftmods.ui.BaseCategoryFragment
import com.delitx.minecraftmods.ui.BaseScrollFragment
import com.delitx.minecraftmods.ui.adapters.CategoriesAdapter
import com.delitx.minecraftmods.ui.adapters.ItemsAdapter
import com.delitx.minecraftmods.viewmodels.BaseCategoriesViewModel
import com.delitx.minecraftmods.viewmodels.SkinsViewModel
import com.delitx.minecraftmods.viewmodels.factory.ViewModelsFactory

class SkinsFragment : BaseCategoryFragment<Skin>() {
    override val mCategoriesAdapter: CategoriesAdapter = CategoriesAdapter(this)
    override val mViewModelClass: Class<out BaseCategoriesViewModel<Skin>> =
        SkinsViewModel::class.java
    override var mFragment: BaseScrollFragment<Skin> =SkinsScrollFragment()
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