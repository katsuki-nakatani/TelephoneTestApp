package example.org.telephonetest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MissedCallNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (ACTION_SHOW_MISSED_CALLS_NOTIFICATION != action) {
            return
        }
        val count = intent.getIntExtra(EXTRA_NOTIFICATION_COUNT,
                UNKNOWN_MISSED_CALL_COUNT)
        val number = intent.getStringExtra(EXTRA_NOTIFICATION_PHONE_NUMBER)


        if (count > 0)
            MissedCallNotificationService.enqueueWork(context, MissedCallNotificationService.createIntent(context, count, number))
    }

    companion object {
        val ACTION_SHOW_MISSED_CALLS_NOTIFICATION = "android.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION"
        val EXTRA_NOTIFICATION_COUNT = "android.telecom.extra.NOTIFICATION_COUNT"
        val EXTRA_NOTIFICATION_PHONE_NUMBER = "android.telecom.extra.NOTIFICATION_PHONE_NUMBER"
        val UNKNOWN_MISSED_CALL_COUNT = -1
    }
}
