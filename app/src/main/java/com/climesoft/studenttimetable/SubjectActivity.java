package com.climesoft.studenttimetable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

public class SubjectActivity extends BaseCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_subject, frameLayout, false);
        frameLayout.addView(coordinatorLayout);
    }
}
