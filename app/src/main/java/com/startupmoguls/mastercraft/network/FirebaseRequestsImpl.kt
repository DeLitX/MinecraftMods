package com.startupmoguls.mastercraft.network

import android.widget.ImageView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.startupmoguls.mastercraft.GlideApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class FirebaseRequestsImpl(private val mInteraction: FirebaseInteraction) : FirebaseRequests {
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mJsons = mFirestore.collection("jsons")
    private val mCategoriesCollection = mFirestore.collection("categories")
    private val mStorage = FirebaseStorage.getInstance()
    private val mRealtimeDB = FirebaseDatabase.getInstance()
    private val mClient: OkHttpClient
    private val DATABASE_BASE_URL = "mastercraft-74b2d.firebaseio.com"

    init {
        mClient = OkHttpClient.Builder().build()
    }


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

    private suspend fun makeRequest(finalSegment:String,doAfter:(String)->Unit) {
        val url = HttpUrl.Builder().scheme("https")
            .host(DATABASE_BASE_URL)
            .addPathSegment("$finalSegment.json")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        val response = call.execute()
        if(response.isSuccessful){
            val json=response.body?.string()
            if(json!=null){
                doAfter(json)
                mInteraction.increaseProgressState()
            }
        }
    }


    override fun getJSONs() {
        CoroutineScope(IO).launch {
            makeRequest("skins_list"){mInteraction.saveSkinJSON(it)}
            makeRequest("mods_list"){mInteraction.saveModJSON(it)}
            makeRequest("maps_list"){mInteraction.saveMapJSON(it)}
        }
    }

    override suspend fun loadFile(link: String, saveTo: File): Boolean {
        if (saveTo.parentFile?.exists() != true) {
            saveTo.parentFile?.mkdirs()
        }
        if (!saveTo.exists()) {
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
        fun increaseProgressState()
        fun onError(error: String)
    }
}