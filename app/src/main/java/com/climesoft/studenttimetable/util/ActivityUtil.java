package com.climesoft.studenttimetable.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActivityUtil {
    public static void moveToActivity(Context context, Class<?> activity){
        if(!isSameActivity(context, activity)){
            Intent i = new Intent(context, activity);
            context.startActivity(i);
        }
    }

    private static boolean isSameActivity(Context context, Class<?> activity){
        return context.getClass().getSimpleName().equals(activity.getSimpleName());
    }
}
