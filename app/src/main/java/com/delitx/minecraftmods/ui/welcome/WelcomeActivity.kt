package com.delitx.minecraftmods.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.delitx.minecraftmods.ModsApp
import com.delitx.minecraftmods.R
import com.delitx.minecraftmods.ui.MainActivity
import kotlinx.android.synthetic.main.welcome_layout.*

class WelcomeActivity : AppCompatActivity() {
    private val SUBSCRIPTION_ID = "minecraft_app_subscription" //TODO get id of subscription
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_layout)
        privacy_policy.visibility = View.GONE
        var steps = 0
        val billingClient = BillingClient.newBuilder(this).enablePendingPurchases()
            .setListener { billingResult, mutableList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && mutableList.isNullOrEmpty()) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.user_cancelled),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.error_occurred) + "." + resources.getString(
                            R.string.try_again
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(p0: BillingResult) {

            }

            override fun onBillingServiceDisconnected() {

            }

        })
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
                    privacy_policy.visibility = View.VISIBLE
                }
                else -> {
                    if (billingClient.isReady) {
                        val skuDetails = SkuDetails(SUBSCRIPTION_ID)
                        val billingFlowParams =
                            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
                        billingClient.launchBillingFlow(this, billingFlowParams)
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.connecting_to_services),
                            Toast.LENGTH_LONG
                        ).show()
                        steps--
                    }
                }
            }
            steps++
        }
    }
}