package example.org.telephonetest

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
class MissedCallNotificationService : JobIntentService() {


    companion object {
        val JOB_ID = 1000
        val GROUP_KEY = "missed_call"
        val CHILD_NOTIFY_TAG = "child_notify"
        val NOTIFICATION_SUMMARY_ID = 1000
        val EXTRA_COUNT = "count"
        val EXTRA_PHONE_NUMBER = "phone_number"

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MissedCallNotificationService::class.java, JOB_ID, work)
        }

        fun createIntent(context: Context, count: Int, phoneNumber: String?): Intent {
            return Intent(context, MissedCallNotificationService::class.java).apply {
                putExtra(EXTRA_COUNT, count)
                putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleWork(intent: Intent) {
        val timeMs = System.currentTimeMillis()
        val publicBuilder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_missed_id))
                .setSmallIcon(R.drawable.ic_stat_call_missed)
                .setColor(resources.getColor(R.color.colorAccent, applicationContext.theme))
                .setGroup(GROUP_KEY)
                .setContentText(getString(R.string.missed_call_public_notification_title, intent.getIntExtra(EXTRA_COUNT,0)))
                .setWhen(timeMs)
                .setAutoCancel(true)
        notifySummary(publicBuilder)
        notifyMissedCall(intent.getStringExtra(EXTRA_PHONE_NUMBER), timeMs, publicBuilder)
    }


    fun notifyMissedCall(phoneNumber: String?, whenTime: Long, publicBuilder: NotificationCompat.Builder) {
        val notificationId = UUID.randomUUID().hashCode()
        val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_missed_id))
                .setColor(resources.getColor(R.color.colorAccent, applicationContext.theme))
                .setSmallIcon(R.drawable.ic_stat_call_missed)
                .setStyle(NotificationCompat.BigTextStyle()
                        .setBigContentTitle(getString(R.string.missed_call_notification_title))
                        .bigText(phoneNumber)
                        .setSummaryText(phoneNumber))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setGroup(GROUP_KEY)
                .setContentText(phoneNumber)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setWhen(whenTime)
                .setPublicVersion(publicBuilder.build())
                .setAutoCancel(true)
        //アクションを追加
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(CHILD_NOTIFY_TAG, notificationId, builder.build())
    }


    fun notifySummary(publicBuilder: NotificationCompat.Builder) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_SUMMARY_ID, NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_missed_id))
                .setSmallIcon(R.drawable.ic_stat_call_missed)
                .setColor(resources.getColor(R.color.colorAccent, applicationContext.theme))
                .setStyle(NotificationCompat.BigTextStyle()
                        .setSummaryText(getString(R.string.missed_call_notification_title)))
                .setGroup(GROUP_KEY)
                .setPublicVersion(publicBuilder.build())
                .setGroupSummary(true).build())

    }
}