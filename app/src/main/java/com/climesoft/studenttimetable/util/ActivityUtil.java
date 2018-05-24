package com.climesoft.studenttimetable.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
}
