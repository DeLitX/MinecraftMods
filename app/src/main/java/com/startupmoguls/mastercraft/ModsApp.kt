package com.startupmoguls.mastercraft

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.startupmoguls.mastercraft.data.database.DataBase

class ModsApp : Application() {
    companion object {
        const val EXTRA_IS_FIRST_OPEN = "extra is first open"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        val repository = Repository.getInstance(DataBase.get(this), this)
        repository.getJSONs()

    }

}