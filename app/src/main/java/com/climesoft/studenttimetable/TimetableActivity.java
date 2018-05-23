package com.climesoft.studenttimetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class TimetableActivity extends BaseCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_time_table, null);
        frameLayout.addView(linearLayout);
    }
}
