package com.startupmoguls.mastercraft.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.pojo.BaseClass
import com.startupmoguls.mastercraft.pojo.Mod
import com.startupmoguls.mastercraft.pojo.Map
import com.startupmoguls.mastercraft.pojo.Skin
import com.startupmoguls.mastercraft.ui.adapters.interfaces.DownloadImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.mods_item.view.*
import kotlinx.android.synthetic.main.rounded_image.view.image_parent

class ItemsAdapter<T>(private val mInteraction: ItemsInteraction<T>, private val mLayoutId: Int) :
    RecyclerView.Adapter<ItemsAdapter.BaseViewHolder<T>>() {
    private var mList = mutableListOf<T>()

    companion object {
        const val SKIN_LAYOUT = 0
        const val MOD_LAYOUT = 1
    }

    fun setList(list: List<T>) {
        mList = list.toMutableList()
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun bind(item: T)
    }

    class SkinsViewHolder<T>(private val v: View, private val mInteraction: ItemsInteraction<T>) :
        BaseViewHolder<T>(v) {
        private val mImage: ImageView = v.findViewById(R.id.item_image)
        private val mIsLiked: CircleImageView = v.findViewById(R.id.liked)

        init {
            v.clipToOutline = true
        }

        override fun bind(item: T) {
            if (item is Skin) {
                mIsLiked.setImageResource(if (item.isFavourite) R.drawable.ic_favorite_liked else R.drawable.ic_favorite_not_liked)
                v.setOnClickListener {
                    mInteraction.onItemClick(item)
                }
                mInteraction.downloadImage(item.images[0], mImage)
                mIsLiked.setOnClickListener {
                    if (item.isFavourite) {
                        mIsLiked.setImageResource(R.drawable.ic_favorite_not_liked)
                        mInteraction.dislikeItem(item)
                    } else {
                        mIsLiked.setImageResource(R.drawable.ic_favorite_liked)
                        mInteraction.likeItem(item)
                    }
                }
            }
        }

    }

    class ItemsViewHolder<T>(private val v: View, private val mInteraction: ItemsInteraction<T>) :
        BaseViewHolder<T>(v) {
        private val mImage: ImageView = v.findViewById(R.id.item_image)
        private val mIsLiked: CircleImageView = v.findViewById(R.id.liked)

        init {
            v.image_parent.clipToOutline = true
        }

        override fun bind(item: T) {
            if (item is BaseClass) {
                mIsLiked.setImageResource(if (item.isFavourite) R.drawable.ic_favorite_liked else R.drawable.ic_favorite_not_liked)
            }
            v.setOnClickListener {
                mInteraction.onItemClick(item)
            }
            mIsLiked.setOnClickListener {
                if (item is BaseClass) {
                    if (item.isFavourite) {
                        mIsLiked.setImageResource(R.drawable.ic_favorite_not_liked)
                        mInteraction.dislikeItem(item)
                    } else {
                        mIsLiked.setImageResource(R.drawable.ic_favorite_liked)
                        mInteraction.likeItem(item)
                    }
                }
            }
            if (item is Mod) {
                v.header.text = item.name
                v.description.text = item.description
                mInteraction.downloadImage(item.images[0], mImage)
            } else if (item is Map) {
                v.header.text = item.name
                v.description.text = item.description
                mInteraction.downloadImage(item.images[0], mImage)
            }
        }

    }

    interface ItemsInteraction<T> : DownloadImage {
        fun onItemClick(item: T)
        fun likeItem(item: T)
        fun dislikeItem(item:T)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(mLayoutId, parent, false)
        return if (viewType == MOD_LAYOUT) ItemsViewHolder(view, mInteraction)
        else SkinsViewHolder(
            view,
            mInteraction
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position] is Skin) SKIN_LAYOUT else MOD_LAYOUT
    }
}