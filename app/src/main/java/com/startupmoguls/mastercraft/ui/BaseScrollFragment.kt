package com.startupmoguls.mastercraft.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginStart
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.ui.adapters.ItemsAdapter
import com.startupmoguls.mastercraft.viewmodels.BaseCategoriesViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory

abstract class BaseScrollFragment<T> : BaseFragment(), ItemsAdapter.ItemsInteraction<T> {
    internal var currentLiveData: LiveData<List<T>>? = null
    internal lateinit var v: View
    internal var mItemsRecycler: RecyclerView? = null
    internal abstract val mItemsAdapter: ItemsAdapter<T>
    internal var mViewModel: BaseCategoriesViewModel<T>? = null
    internal abstract val mViewModelClass: Class<out BaseCategoriesViewModel<T>>
    internal var isFavourites = false
    internal var isUpdate = true
    private var isMakeMargin = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mItemsRecycler = RecyclerView(this.requireContext())
        setupViewModel(mViewModelClass)
        bindFragment()
        tuneViewModel()
        actionsOnStart()
        return mItemsRecycler
    }

    override fun downloadImage(path: String, imageHolder: ImageView) {
        mViewModel?.downloadPhoto(path, imageHolder)
    }

    private fun setupViewModel(modelClass: Class<out BaseCategoriesViewModel<T>>) {
        val viewModelFactory = ViewModelsFactory(
            requireActivity().application, Repository.getInstance(
                DataBase.get(requireActivity().application), requireActivity().application
            )
        )
        mViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(modelClass)
    }

    private fun tuneViewModel() {
        if (isFavourites) {
            selectFavourites()
        } else {
            mViewModel?.getItems()?.let { switchToLiveData(it) }
        }
    }

    private fun bindFragment() {
        setupItemsRecycler()
        if (isMakeMargin) {
            marginHorizontal()
        }
    }

    fun selectFavourites() {
        if (mViewModel != null) {
            switchToLiveData(mViewModel!!.getFavourites())
        } else {
            isFavourites = true
        }
    }

    fun marginHorizontal() {
        if (mItemsRecycler != null) {
            mItemsRecycler!!.setPadding(
                resources.getDimension(R.dimen.margin_border).toInt(),
                0,
                resources.getDimension(R.dimen.margin_border).toInt(),
                0
            )
        } else {
            isMakeMargin = true
        }
    }


    internal abstract fun actionsOnStart()
    internal abstract fun setupItemsRecycler()
    internal abstract fun search(query: String)

    fun selectCategory(name: String) {
        mViewModel?.getItems(name)?.let { switchToLiveData(it) }
    }

    internal fun switchToLiveData(liveData: LiveData<List<T>>) {
        currentLiveData?.removeObservers(this)
        currentLiveData = liveData
        currentLiveData?.observe(viewLifecycleOwner) {
            if (isUpdate) {
                mItemsAdapter.setList(it)
            } else {
                isUpdate = true
            }
        }
    }

    override fun dislikeItem(item: T) {
        isUpdate = false
        mViewModel?.dislikeItem(item)
    }

    override fun likeItem(item: T) {
        isUpdate = false
        mViewModel?.likeItem(item)
    }

}