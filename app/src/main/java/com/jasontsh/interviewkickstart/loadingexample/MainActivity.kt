package com.jasontsh.interviewkickstart.loadingexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import com.jasontsh.interviewkickstart.loadingexample.Constants.FAILURE
import com.jasontsh.interviewkickstart.loadingexample.Constants.INTENT_FILTER

class MainActivity : AppCompatActivity() {
    private lateinit var broadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LoadingService::class.java)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        val manager = supportFragmentManager
        manager.commit {
            add(R.id.frame, ItemFragment())
        }
        val model : MyViewModel by viewModels()
        broadcastReceiver = createBroadcastReceiver(model)
        startService(intent)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction(INTENT_FILTER)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        unregisterReceiver(broadcastReceiver)
        super.onPause()
    }

    private fun createBroadcastReceiver(model: MyViewModel) = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == INTENT_FILTER) {
                val failure = intent.getBooleanExtra(FAILURE, false)
                if (failure) {
                    model.failure.value = true
                } else {
                    val list = intent.getDoubleArrayExtra(Constants.DATA_KEY)
                    model.data.value = list
                }
            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, LoadingService::class.java))
        super.onDestroy()
    }
}