package com.startupmoguls.mastercraft.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.pojo.Category

class CategoriesAdapter(private val mInteraction: CategoriesInteraction) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var mList = mutableListOf<Category>()
    private var currentFocus:Int=0
    fun setList(list: List<Category>) {
        mList = list.toMutableList()
        notifyDataSetChanged()
    }

    class CategoriesViewHolder(
        private val v: View,
        private val mInteraction: CategoriesInteraction
    ) : RecyclerView.ViewHolder(v) {
        private val mName: TextView = v.findViewById(R.id.category)
        fun bind(category: Category, isChosen: Boolean) {
            v.setOnClickListener {
                mInteraction.onCategorySelect(category)
            }
            mName.text = category.shown
            mName.setTextColor(v.resources.getColor(if (isChosen) R.color.colorPrimaryDark else R.color.grayish))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.category_item, parent, false)
        return CategoriesViewHolder(view, mInteraction)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(mList[position],currentFocus==position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    fun changeFocus(name:Category){
        changeFocus(mList.indexOf(name))
    }

    fun changeFocus(toPosition:Int){
        val prev=currentFocus
        currentFocus=toPosition
        notifyItemChanged(prev)
        notifyItemChanged(toPosition)
    }

    interface CategoriesInteraction {
        fun onCategorySelect(category: Category)
    }
}