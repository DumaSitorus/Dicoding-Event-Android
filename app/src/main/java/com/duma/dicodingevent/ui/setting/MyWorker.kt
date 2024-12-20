package com.duma.dicodingevent.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.duma.dicodingevent.R
import com.duma.dicodingevent.data.response.ListEventsItem
import com.duma.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        createNotificationChannel()

        val nearestEvent = runBlocking {
            getNearestEvent()
        }

        nearestEvent?.let {
            sendNotification(it.name, it.beginTime)
        }

        return Result.success()
    }

    private suspend fun getNearestEvent(): ListEventsItem? {
        return try {
            val response = ApiConfig.getApiService().getNearestEvents(-1, 1)
            response.listEvents.firstOrNull()
        } catch (e: Exception) {
            Log.e("MyWorker", "Failed to fetch nearest event: ${e.message}")
            null
        }
    }

    private fun sendNotification(eventName: String, eventDate: String) {
        Log.d("MyWorker", "Sending notification for event: $eventName at $eventDate")
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val notification = NotificationCompat.Builder(applicationContext, "reminder_channel")
            .setSmallIcon(R.drawable.ic_round_event_black_24dp)
            .setContentTitle("Upcoming Event Reminder")
            .setContentText("Don't forget: $eventName on $eventDate")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Daily Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Daily Reminder"
            }
            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}