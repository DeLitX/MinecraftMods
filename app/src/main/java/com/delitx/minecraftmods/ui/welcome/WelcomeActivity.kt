package com.delitx.minecraftmods.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.delitx.minecraftmods.ModsApp
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.ui.MainActivity
import kotlinx.android.synthetic.main.welcome_layout.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_layout)
        privacy_policy.visibility = View.GONE
        var steps = 0
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_placeholder, WelcomeFragment1()).commit()
        button.setOnClickListener {
            when (steps) {
                0 -> supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_placeholder,
                    WelcomeFragment2(resources.getString(R.string.choose_mods))
                ).commit()
                1 -> supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_placeholder,
                    WelcomeFragment2(resources.getString(R.string.choose_skins))
                ).commit()
                2 -> supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_placeholder,
                    WelcomeFragment2(resources.getString(R.string.choose_maps))
                ).commit()
                3 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder, WelcomeFragment3()).commit()
                    privacy_policy.visibility=View.VISIBLE
                }
                else->{
                    val sharedPrefs=getSharedPreferences("shared prefs", MODE_PRIVATE)
                    sharedPrefs.edit().putBoolean(ModsApp.EXTRA_IS_FIRST_OPEN,false).commit()
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            steps++
        }
    }
}