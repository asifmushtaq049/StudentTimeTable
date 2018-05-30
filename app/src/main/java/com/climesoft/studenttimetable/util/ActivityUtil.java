package com.climesoft.studenttimetable.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.climesoft.studenttimetable.broadcast.NotificationReceiver;

import java.util.Calendar;

public class ActivityUtil {
    public static void moveToActivity(Context context, Class<?> activity){
        moveToActivity(context, activity, null);
    }

    public static void moveToActivity(Context context, Class<?> activity, Bundle bundle){
        if(!isSameActivity(context, activity)){
            Intent i = new Intent(context, activity);
            if(bundle != null)
                i.putExtras(bundle);
            context.startActivity(i);
        }
    }

    private static boolean isSameActivity(Context context, Class<?> activity){
        return context.getClass().getSimpleName().equals(activity.getSimpleName());
    }

    public static void showMessage(Context context, String txt){
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }

    public static int dayToInt(String day){
        switch (day){
            case "Monday":
                return Calendar.MONDAY;
            case "Tuesday":
                return Calendar.TUESDAY;
            case "Wednesday":
                return Calendar.WEDNESDAY;
            case "Thursday":
                return Calendar.THURSDAY;
            case "Friday":
                return Calendar.FRIDAY;
            case "Saturday":
                return Calendar.SATURDAY;
            case "Sunday":
                return Calendar.SUNDAY;
            default:
                return 0;
        }
    }

    public static void setAlarm(Context context, Calendar calendar, Bundle bundle, int hashCode){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent _myIntent = new Intent(context, NotificationReceiver.class);
        _myIntent.putExtras(bundle);
        PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), hashCode, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),_myPendingIntent);

    }

    public static void setTimeTableAlarm(Context context, Calendar calendar, Bundle bundle, int hashCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent _myIntent = new Intent(context, NotificationReceiver.class);
        _myIntent.putExtras(bundle);
        PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), hashCode, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, _myPendingIntent);
        Log.d("ALARMTIME:", String.valueOf(calendar.getTimeInMillis()));
    }
}
