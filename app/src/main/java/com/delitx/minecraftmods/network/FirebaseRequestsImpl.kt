package com.delitx.minecraftmods.network

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.delitx.minecraftmods.GlideApp
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.pojo.Category
import com.delitx.minecraftmods.pojo.Mod
import com.delitx.minecraftmods.pojo.Map
import com.delitx.minecraftmods.pojo.Skin
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File

class FirebaseRequestsImpl(private val mInteraction: FirebaseInteraction) : FirebaseRequests {
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mJsons = mFirestore.collection("jsons")
    private val mCategoriesCollection = mFirestore.collection("categories")
    private val mStorage = FirebaseStorage.getInstance()


    private fun getCategories(folderName: String, actionSave: (List<String>) -> Unit) {
        mCategoriesCollection.document(folderName).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val list: List<String> = it.get("categories") as List<String>
                    actionSave(list)
                }
            }.addOnFailureListener {
                mInteraction.onError(it.toString())
            }
    }


    override fun getJSONs() {
        mJsons.document("map").get().addOnSuccessListener {
            if (it.exists()) {
                val json = it.getString("json")
                if (json != null) {
                    mInteraction.saveMapJSON(json)
                }
            }
        }.addOnFailureListener {
            mInteraction.onError(it.toString())
        }
        mJsons.document("mod").get().addOnSuccessListener {
            if (it.exists()) {
                val json = it.getString("json")
                if (json != null) {
                    mInteraction.saveModJSON(json)
                }
            }
        }.addOnFailureListener {
            mInteraction.onError(it.toString())
        }
        mJsons.document("skin").get().addOnSuccessListener {
            if (it.exists()) {
                val json = it.getString("json")
                if (json != null) {
                    mInteraction.saveSkinJSON(json)
                }
            }
        }.addOnFailureListener {
            mInteraction.onError(it.toString())
        }
    }

    override suspend fun loadFile(link: String, saveTo: File): Boolean {
        if(saveTo.parentFile?.exists()!=true){
            saveTo.parentFile?.mkdirs()
        }
        if(!saveTo.exists()){
            saveTo.createNewFile()
        }
        var succeed = true
        val storageRef = mStorage.getReference(link).getFile(saveTo).addOnFailureListener {
            succeed = false
        }
        Tasks.await(storageRef)
        return succeed
    }

    override fun loadImage(link: String, imageHolder: ImageView) {
        if (link.isNotEmpty()) {
            GlideApp.with(imageHolder.context).load(mStorage.getReference(link))
                .into(imageHolder)
        }
    }

    interface FirebaseInteraction {
        fun saveMapJSON(json: String)
        fun saveModJSON(json: String)
        fun saveSkinJSON(json: String)
        fun onError(error: String)
    }
}