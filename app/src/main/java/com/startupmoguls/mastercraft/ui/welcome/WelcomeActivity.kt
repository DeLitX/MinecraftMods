package com.startupmoguls.mastercraft.ui.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.ui.MainActivity
import kotlinx.android.synthetic.main.welcome_layout.*

class WelcomeActivity : AppCompatActivity() {
    private val SUBSCRIPTION_ID = "minecraft_app_subscription" //TODO get id of subscription
    private val PRIVACY_LINK =
        "https://docs.google.com/document/d/1Z-m-ljXQ2yCQxTGodv_SOvaCKAfNyqScl6Zv9XTn4cY/edit"
    private val TERMS_LINK =
        "https://docs.google.com/document/d/1B7Pvy3pDp_LKO6aEKMVNd20HARCg9XxSE3iEwrQdWh4/edit"
    private var mSkuDetails: SkuDetails? = null
    private var mIsSkuInProcess = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_layout)
        //I know that this is bad,but i'm running out of time

        var steps = 0
        val billingClient = BillingClient.newBuilder(this).enablePendingPurchases()
            .setListener { billingResult, mutableList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !mutableList.isNullOrEmpty()) {
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
                val param = SkuDetailsParams.newBuilder()
                    .setType(BillingClient.SkuType.SUBS)
                    .setSkusList(
                        listOf(
                            SUBSCRIPTION_ID
                        )
                    )
                    .build()
                mIsSkuInProcess = true
                billingClient.querySkuDetailsAsync(param) { billingResult: BillingResult, mutableList: MutableList<SkuDetails>? ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && mutableList != null && mutableList.size != 0) {
                        mSkuDetails = mutableList[0]
                        mIsSkuInProcess = false
                    }
                }
            }

            override fun onBillingServiceDisconnected() {

            }

        })
        video.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.welcome_screen_video))
        video.start()
        video.setOnCompletionListener {
            video.start()
        }
        privacy_policy.setOnClickListener {
            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_LINK))
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(marketIntent)
        }
        terms_of_use.setOnClickListener {
            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_LINK))
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(marketIntent)
        }
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
                    ampersant.visibility = View.VISIBLE
                    terms_of_use.visibility = View.VISIBLE
                }
                else -> {
                    if (billingClient.isReady && !mIsSkuInProcess && mSkuDetails != null) {
                        val billingFlowParams =
                            BillingFlowParams.newBuilder().setSkuDetails(mSkuDetails!!).build()
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
        button.setOnTouchListener { view, motionEvent ->
            if(motionEvent.action==MotionEvent.ACTION_DOWN){
                button_active.visibility=View.VISIBLE
                button.visibility=View.INVISIBLE
            }else if(motionEvent.action==MotionEvent.ACTION_UP){
                button_active.visibility=View.INVISIBLE
                button.visibility=View.VISIBLE
                view.performClick()
            }
            true
        }
    }

    override fun onRestart() {
        super.onRestart()
        video.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.welcome_screen_video))
        video.start()
        video.setOnCompletionListener {
            video.start()
        }
    }
}