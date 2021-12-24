package com.jasontsh.interviewkickstart.loadingexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jasontsh.interviewkickstart.loadingexample.Constants.DATA_KEY
import com.jasontsh.interviewkickstart.loadingexample.Constants.FAILURE
import com.jasontsh.interviewkickstart.loadingexample.Constants.NUMBER_OF_ITEMS
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

class LoadingService : Service() {
    private var finalResult = false
    private val semaphore = Semaphore(1)
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val executorService: ExecutorService = Executors.newFixedThreadPool(2)
        executorService.submit {
            val list = ArrayList<Double>(NUMBER_OF_ITEMS)
            while (list.size < NUMBER_OF_ITEMS) {
                val failure = Math.random() > 0.999999999
                if (failure) {
                    val broadcastIntent = Intent()
                    broadcastIntent.action = Constants.INTENT_FILTER
                    broadcastIntent.putExtra(FAILURE, true)
                    sendBroadcast(broadcastIntent)
                }
                val i = Math.random()
                if (i > 0.999995) {
                    list.add(i)
                }
            }
            semaphore.acquire()
            if (!finalResult) {
                val broadcastIntent = Intent()
                broadcastIntent.action = Constants.INTENT_FILTER
                broadcastIntent.putExtra(DATA_KEY, list.toDoubleArray())
                broadcastIntent.putExtra(FAILURE, false)
                sendBroadcast(broadcastIntent)
                finalResult = true
            }
            semaphore.release()
        }
        val list = ArrayList<Double>(NUMBER_OF_ITEMS)
        while (list.size < NUMBER_OF_ITEMS) {
            val failure = Math.random() > 0.99999999
            if (failure) {
                val broadcastIntent = Intent()
                broadcastIntent.action = Constants.INTENT_FILTER
                broadcastIntent.putExtra(FAILURE, true)
                sendBroadcast(broadcastIntent)
                return START_NOT_STICKY
            }
            val i = Math.random()
            if (i < 0.00001) {
                list.add(i)
            }
        }
        semaphore.acquire()
        if (!finalResult) {
            val broadcastIntent = Intent()
            broadcastIntent.action = Constants.INTENT_FILTER
            broadcastIntent.putExtra(DATA_KEY, list.toDoubleArray())
            broadcastIntent.putExtra(FAILURE, false)
            sendBroadcast(broadcastIntent)
        }
        semaphore.release()
        return START_NOT_STICKY
    }
}