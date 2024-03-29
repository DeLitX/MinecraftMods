package com.startupmoguls.mastercraft.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.pojo.BaseClass
import com.startupmoguls.mastercraft.pojo.Category
import com.startupmoguls.mastercraft.ui.adapters.CategoriesAdapter
import com.startupmoguls.mastercraft.ui.adapters.FragmentFavouritesAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import kotlinx.android.synthetic.main.items_fragment.view.*

abstract class BaseCategoryFragment<T: BaseClass> : BaseFragment(), CategoriesAdapter.CategoriesInteraction {
    internal lateinit var v: View
    internal var currentCategory: String = ""
    internal lateinit var mCategoriesRecycler: RecyclerView
    internal abstract val mCategoriesAdapter: CategoriesAdapter
    internal lateinit var mViewModel: BaseCategoriesViewModel<T>
    internal abstract val mViewModelClass: Class<out BaseCategoriesViewModel<T>>
    internal lateinit var mSearchView: SearchView
    internal abstract var mFragment: BaseScrollFragment<T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.items_fragment, container, false)
        setupViewModel()
        bindFragment()
        tuneViewModel()
        actionsOnStart()
        v.items_container.adapter= FragmentFavouritesAdapter(listOf(mFragment),this){}
        v.items_container.isUserInputEnabled=false
        return v
    }

    override fun onStart() {
        super.onStart()
        mViewModel.refreshCategories()
    }

    internal abstract fun tuneViewModel()

    internal abstract fun setupViewModel()

    internal fun selectCategory(name: String){
        mFragment.selectCategory(name)
    }
    internal abstract fun actionsOnStart()
    internal fun search(query:String){
        mFragment.search(query)
    }
    private fun bindFragment() {
        mSearchView = v.findViewById(R.id.search)
        setupCategoriesRecycler()
    }

    private fun setupCategoriesRecycler() {
        mCategoriesRecycler = v.findViewById(R.id.categories_recycler)
        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mCategoriesRecycler.layoutManager = layoutManager
        mCategoriesRecycler.adapter = mCategoriesAdapter
    }

    override fun onCategorySelect(category: Category) {
        currentCategory = category.real
        selectCategory(currentCategory)
        mCategoriesAdapter.changeFocus(category)
    }
}