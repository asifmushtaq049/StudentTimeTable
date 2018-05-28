package com.climesoft.studenttimetable;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;

public class TimeTableDetailActivity extends BaseCompatActivity {

    private String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) LayoutInflater
                .from(this).inflate(R.layout.activity_time_table_detail, null);
        frameLayout.addView(coordinatorLayout);
        if(getIntent()!=null){
            day = getIntent().getStringExtra(KeyMeta.DAY);
        }
        floatingButtonAction(TimeTableAddActivity.class);
    }

    @Override
    protected void floatingButtonAction(final Class<?> desActivity){
        FloatingActionButton fab = findViewById(R.id.fab);
        final Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, day);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.moveToActivity(TimeTableDetailActivity.this, desActivity, bundle);
            }
        });
    }
}
