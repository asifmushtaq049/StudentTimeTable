package com.climesoft.studenttimetable.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.climesoft.studenttimetable.R;
import com.climesoft.studenttimetable.meta.KeyMeta;

public class NotificationReceiver extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(KeyMeta.TYPE);
        String CHANNEL_ID = "StudentTimeTable";
        if(type.equals(KeyMeta.QUIZ) || type.equals(KeyMeta.ASSIGNMENT)){
            String topic = intent.getStringExtra(KeyMeta.QUIZ_ASS_TOPIC);
            String subject = intent.getStringExtra(KeyMeta.QUIZ_ASS_SUBJECT);
            int notificationId = intent.getIntExtra(KeyMeta.HASH_CODE, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(type)
                    .setContentText(subject.toUpperCase() + " " + type.toUpperCase())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("You have a scheduled " + type + " of " +subject + ". The topic is " + topic))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, mBuilder.build());
        }
        if(type.equals(KeyMeta.TIMETABLE)){
            String subject = intent.getStringExtra(KeyMeta.SUBJECT);
            int notificationId = intent.getIntExtra(KeyMeta.HASH_CODE, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(type)
                    .setContentText("You have a scheduled lecture of " +subject.toUpperCase() + ".")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, mBuilder.build());
        }


    // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notify = context.getSystemService(NotificationManager.class);
//            notify.createNotificationChannel(channel);
//        }

    }
}
