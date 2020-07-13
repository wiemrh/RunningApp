package com.wiem.runningapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.wiem.runningapp.R
import com.wiem.runningapp.other.Constants.ACTION_PAUSE_SERVICE
import com.wiem.runningapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.wiem.runningapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.wiem.runningapp.other.Constants.ACTION_STOP_SERVICE
import com.wiem.runningapp.other.Constants.NOTIFICATION_CHANNEL_ID
import com.wiem.runningapp.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.wiem.runningapp.other.Constants.NOTIFICATION_ID
import com.wiem.runningapp.ui.MainActivity
import timber.log.Timber

class TrackingService : LifecycleService() {
    var isFirstRun = true
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager //System service for android , that we need whenever we want to show notification

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,//bcz we don't need this req code
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT //whenever we launch that pending intent and it already exists it will update it instead of restarting / recreating it
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW //each time we update our notification the phone will ring , but we don't want that
        )
        notificationManager.createNotificationChannel(channel)
    }
}