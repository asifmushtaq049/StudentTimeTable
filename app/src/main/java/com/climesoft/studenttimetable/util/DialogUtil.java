package com.climesoft.studenttimetable.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogUtil {

    public static void showDialog(Context context,int layout, String title, String btn1, String btn2,
                                  DialogInterface.OnClickListener yes,
                                  DialogInterface.OnClickListener no,
                                  boolean cancelable){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setView(layout);
        builder1.setCancelable(cancelable);
        builder1.setPositiveButton(btn1, yes);
        builder1.setNegativeButton(btn2, no);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showDialog(Context context,int layout, String title, String btn1, String btn2,
                                  DialogInterface.OnClickListener yes,
                                  DialogInterface.OnClickListener no){
        showDialog(context, layout, title, btn1, btn2, yes, no, true);
    }

    public static void showDialog(Context context, int layout, String btn1, String btn2,
                                  DialogInterface.OnClickListener yes,
                                  DialogInterface.OnClickListener no){
        showDialog(context, layout,
                context.getClass().getSimpleName().replace("Activity", ""),
                btn1, btn2, yes, no, true);
    }

}
