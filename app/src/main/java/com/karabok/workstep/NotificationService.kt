package com.karabok.workstep

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder

class NotificationService : Service() {

    private val handler = Handler()
    private lateinit var notificationHelper: NotificationHelper
    private var isServiceRunning = false

    private val showNotificationTask = object : Runnable {
        override fun run() {
            // Отправка уведомления
            notificationHelper.createNotification("Напоминание", "Пора проверить что-то важное!")
            // Планирование следующего уведомления через 5 секунд
            handler.postDelayed(this, 5000L)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isServiceRunning) {
            handler.postDelayed(showNotificationTask, 0L) // Запускаем первое уведомление
            isServiceRunning = true
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacks(showNotificationTask)
        isServiceRunning = false
        super.onDestroy()
    }
}