package com.climesoft.studenttimetable.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.climesoft.studenttimetable.broadcast.NotificationReceiver;
import com.climesoft.studenttimetable.meta.KeyMeta;

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

        if(KeyMeta.CUSTOM.equals(bundle.getString(KeyMeta.TYPE))){
            calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(customBefore(context)[0]));
            calendar.add(Calendar.MINUTE, -Integer.parseInt(customBefore(context)[1]));
        }
        if(KeyMeta.ASSIGNMENT.equals(bundle.getString(KeyMeta.TYPE))){
            calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(assignmentBefore(context)[0]));
            calendar.add(Calendar.MINUTE, -Integer.parseInt(assignmentBefore(context)[1]));
        }
        if(KeyMeta.QUIZ.equals(bundle.getString(KeyMeta.TYPE))){
            calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(quizBefore(context)[0]));
            calendar.add(Calendar.MINUTE, -Integer.parseInt(quizBefore(context)[1]));
        }


        PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), hashCode, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),_myPendingIntent);

    }

    public static void setTimeTableAlarm(Context context, Calendar calendar, Bundle bundle, int hashCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent _myIntent = new Intent(context, NotificationReceiver.class);
        calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(timeBefore(context)[0]));
        calendar.add(Calendar.MINUTE, -Integer.parseInt(timeBefore(context)[1]));
        _myIntent.putExtras(bundle);
        PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), hashCode, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, _myPendingIntent);
        Log.d("ALARMTIME:", String.valueOf(calendar.getTimeInMillis()));
    }

    public static String[] timeBefore(Context context){
        SharedPreferences prefs;
        prefs = context.getApplicationContext().getSharedPreferences(KeyMeta.PREF_SETTING, Context.MODE_PRIVATE);
        String time = prefs.getString(KeyMeta.PREF_SETTING_TIMETABLE, "0:15");
        String[] comTime = time.split(":");
        return comTime;
    }
    public static String[] quizBefore(Context context){
        SharedPreferences prefs;
        prefs = context.getApplicationContext().getSharedPreferences(KeyMeta.PREF_SETTING, Context.MODE_PRIVATE);
        String time = prefs.getString(KeyMeta.PREF_SETTING_QUIZ, "24:00");
        String[] comTime = time.split(":");
        return comTime;
    }
    public static String[] assignmentBefore(Context context){
        SharedPreferences prefs;
        prefs = context.getApplicationContext().getSharedPreferences(KeyMeta.PREF_SETTING, Context.MODE_PRIVATE);
        String time = prefs.getString(KeyMeta.PREF_SETTING_ASSIGNMENT, "24:00");
        String[] comTime = time.split(":");
        return comTime;
    }
    public static String[] customBefore(Context context){
        SharedPreferences prefs;
        prefs = context.getApplicationContext().getSharedPreferences(KeyMeta.PREF_SETTING, Context.MODE_PRIVATE);
        String time = prefs.getString(KeyMeta.PREF_SETTING_CUSTOM, "24:00");
        String[] comTime = time.split(":");
        return comTime;
    }
}
