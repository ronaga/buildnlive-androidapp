package buildnlive.com.buildlive.Notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import buildnlive.com.buildlive.R
import buildnlive.com.buildlive.activities.NotificationActivity
import buildnlive.com.buildlive.console
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    var id = 0

    override fun onCreate() {
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)


//        if(remoteMessage!!.notification==null) {
        if (remoteMessage!!.data.isNotEmpty()) {

//                if (remoteMessage.data["notification_type"].equals("Reminder")) {

            sendNotification(remoteMessage.data["body"], remoteMessage.data["title"],
                    remoteMessage.data["click_action"], remoteMessage.data["notification_type"], remoteMessage.data["user_id"], remoteMessage.data["project_id"])

//                }
//                else {
//                    sendNotificationDefault(remoteMessage.data["body"], remoteMessage.data["title"])
//                    sendNotificationDefault(remoteMessage.notification!!.body, remoteMessage.notification!!.title)
//                }


        }
//        }
//        else {
//            sendNotificationDefault(remoteMessage.notification!!.body,remoteMessage.notification!!.title)
//        }
    }

    private fun sendNotificationDefault(message: String?, title: String?) {
        val intent = Intent(this, NotificationActivity::class.java).apply { Intent.FLAG_ACTIVITY_CLEAR_TOP }

        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "example.permanence"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_buildnlive_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(s: String?) {
        console.log("FCM Token $s")
        getSharedPreferences("Token", MODE_PRIVATE).edit().putString("FcmToken", s).apply();
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("FcmToken", "")
    }


    private fun sendNotification(message: String?, title: String?, clickAction: String?, notificationType: String?, user_id: String?, project_id: String?) {
        id += 1
        val intent = Intent(clickAction)
        intent.putExtra("title", title)
        intent.putExtra("notification_type", notificationType)
        intent.putExtra("body", message)
        intent.putExtra("user_id", user_id)
        intent.putExtra("project_id", project_id)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "buildo.notification"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setOngoing(false)
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setGroup("fcm")
                .setContentIntent(pendingIntent)

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(title)
        bigTextStyle.bigText(message)

        notificationBuilder.setStyle(bigTextStyle)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build())

    }
}
