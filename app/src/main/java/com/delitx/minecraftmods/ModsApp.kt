package com.delitx.minecraftmods

import android.app.Application
import com.android.billingclient.api.*
import com.delitx.minecraftmods.data.database.DataBase

class ModsApp : Application() {
    companion object {
        const val EXTRA_IS_FIRST_OPEN = "extra is first open"
    }

    override fun onCreate() {
        super.onCreate()
        val repository = Repository.getInstance(DataBase.get(this), this)
        repository.getJSONs()

    }

}