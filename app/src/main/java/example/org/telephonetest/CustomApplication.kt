package example.org.telephonetest

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(NotificationChannel(getString(R.string.channel_missed_id), getString(R.string.channel_missed_label), NotificationManager.IMPORTANCE_DEFAULT)
                    .apply {
                        setSound(null,null)
                        enableLights(true)
                        enableVibration(true)
                        lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    })

        }
    }
}