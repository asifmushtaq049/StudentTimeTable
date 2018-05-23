package com.climesoft.studenttimetable;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class QuizActivity extends BaseCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_quiz, frameLayout, false);
        frameLayout.addView(coordinatorLayout);
    }

}
