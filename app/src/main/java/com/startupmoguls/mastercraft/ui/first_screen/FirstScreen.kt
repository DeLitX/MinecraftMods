package com.startupmoguls.mastercraft.ui.first_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.billingclient.api.*
import com.startupmoguls.mastercraft.R
import com.startupmoguls.mastercraft.Repository
import com.startupmoguls.mastercraft.data.database.DataBase
import com.startupmoguls.mastercraft.ui.MainActivity
import com.startupmoguls.mastercraft.ui.welcome.WelcomeActivity
import com.startupmoguls.mastercraft.viewmodels.FirstScreenViewModel
import com.startupmoguls.mastercraft.viewmodels.factory.ViewModelsFactory
import kotlinx.android.synthetic.main.activity_first_screen.*

class FirstScreen : AppCompatActivity() {
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mProgressText: TextView
    private lateinit var mNoConnection: TextView
    private lateinit var mTryAgain: TextView
    private lateinit var mError: TextView
    private lateinit var mViewModel: FirstScreenViewModel
    private val PROGRESS_STATES = listOf(9, 24, 47, 62, 86, 100)
    private var mIsSubscribed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)
        setupViewModel()
        bindActivity()
        connect()
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProviders.of(
            this, ViewModelsFactory(
                this.application, Repository.getInstance(
                    DataBase.get(this.application), this.application
                )
            )
        ).get(FirstScreenViewModel::class.java)
    }

    private fun connect() {
        val billingClient = BillingClient.newBuilder(this)
            .setListener { p0, p1 -> }
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkForSubscription(billingClient)
                } else {
                    setErrorOccured()
                }
            }

            override fun onBillingServiceDisconnected() {
                billingClient.endConnection()
                setNoConnection()
            }

        })
    }

    private fun checkForSubscription(billingClient: BillingClient) {
        val purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
        val billingResult = purchases.billingResult
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            if (!purchases.purchasesList.isNullOrEmpty() && (purchases.purchasesList!![0].isAutoRenewing || purchases.purchasesList!![0].purchaseState == Purchase.PurchaseState.PURCHASED)) {
                mIsSubscribed = true
            } else {
                mIsSubscribed = false
            }
            mViewModel.increaseProgressState()
        } else {
            setErrorOccured()
            mIsSubscribed = false
        }

    }

    private fun bindActivity() {
        mProgressBar = enter_progress
        mProgressText = percent_progress
        mNoConnection = no_connection
        mTryAgain = try_again
        mError = error_occurred
        mTryAgain.setOnClickListener {
            setTryToConnect()
            connect()
        }
        setTryToConnect()
        mViewModel.progressState.observe(this) {
            mProgressText.text = "${PROGRESS_STATES[it]}%"
            mProgressBar.progress = PROGRESS_STATES[it]
            if (it >= 4) {
                if (mIsSubscribed) {
                    goToMainScreen()
                } else {
                    goToWelcomeScreen()
                }
            }
        }
    }

    private fun setNoConnection() {
        mProgressBar.visibility = View.GONE
        mProgressText.visibility = View.GONE
        mError.visibility = View.GONE
        mNoConnection.visibility = View.VISIBLE
        mTryAgain.visibility = View.VISIBLE
    }

    private fun setTryToConnect() {
        mProgressBar.visibility = View.VISIBLE
        mProgressText.visibility = View.VISIBLE
        mNoConnection.visibility = View.GONE
        mTryAgain.visibility = View.GONE
        mError.visibility = View.GONE
        mViewModel.resetProgressState()
    }

    private fun setErrorOccured() {
        mProgressBar.visibility = View.GONE
        mProgressText.visibility = View.GONE
        mNoConnection.visibility = View.VISIBLE
        mTryAgain.visibility = View.VISIBLE
        mError.visibility = View.VISIBLE
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToWelcomeScreen() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}