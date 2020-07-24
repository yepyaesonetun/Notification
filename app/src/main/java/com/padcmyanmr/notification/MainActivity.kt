package com.padcmyanmr.notification

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.Notification.VISIBILITY_PRIVATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val CHANNEL_ID = "PADCNOTIFICATION_CHANNEL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createNotificationChannel()

        val notificationId : Int = 1996
        val notificationId02 : Int = 1997
        val notificationId03 : Int = 1998
        val notificationId04 : Int = 1999
        val notificationId05 : Int = 2000


        /***
         * Just Notification
         */
        var textTitle = "Hello Friend"
        var textContent = "I am a New Notification"

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        button.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }


        /***
         * Notification with Intent
         */
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder2 = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        btnNotiWithIntent.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId02, builder2.build())
            }
        }

        /***
         * Notification with Actions
         */
        val snoozeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
        val builder3 = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Hello Action")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
//                .setFullScreenIntent(pendingIntent, true)  // urgent noti type only use
//                .setVisibility(VISIBILITY_PRIVATE)   // visibility modes
                .addAction(android.R.drawable.star_off, "Snooze",
                        snoozePendingIntent)

        btnNotiWithActions.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId03, builder3.build())
            }
        }


        /***
         * Notification with Progress
         */
        val builder4 = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(android.R.drawable.stat_sys_download)
        }
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 10
        NotificationManagerCompat.from(this).apply {
            // Issue the initial notification with zero progress
            builder4.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)

            // Do the job here that tracks the progress.
            // Usually, this should be in a
            // worker thread
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            // notificationManager.notify(notificationId, builder.build());

            // When done, update the notification one more time to remove the progress bar
            builder.setContentText("Download complete")
                    .setProgress(0, 0, false)

            btnProgress.setOnClickListener {
                notify(notificationId04, builder4.build())
            }
        }


        /***
         * Notification with Image
         */

        // Get drawable from resource
        val drawable: Drawable? = ContextCompat.getDrawable(this,R.drawable.day)

        // Convert drawable to bitmap
        val bitmap: Bitmap? = drawable?.toBitmap()

        val builder5 = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("imageTitle")
            .setContentText("imageDescription")
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(bitmap))  // null

        btnBigImage.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId05, builder5.build())
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PADC Myanmar"
            val descriptionText = "PADC Learning Portal"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}


