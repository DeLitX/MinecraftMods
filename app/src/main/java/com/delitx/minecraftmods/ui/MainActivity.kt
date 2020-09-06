package com.delitx.minecraftmods.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.delitx.minecraftmods.ModsApp
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.Repository
import com.delitx.minecraftmods.data.database.DataBase
import com.delitx.minecraftmods.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //check is app opened first time
        if (getSharedPreferences(
                "shared prefs",
                MODE_PRIVATE
            ).getBoolean(ModsApp.EXTRA_IS_FIRST_OPEN, true)
        ) {
            val intent= Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        Repository.getInstance(DataBase.get(this.application), this.application).recreateDB()
    }
}