package com.climesoft.studenttimetable;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.climesoft.studenttimetable.adapters.QuizAdapter;
import com.climesoft.studenttimetable.adapters.TimeTableAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.firebase.firestore.Query;

public class TimeTableDetailActivity extends BaseCompatActivity {

    private String day;
    private RecyclerView recyclerView;
    private TimeTableAdapter mAdapter;
    private Query query;


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
        recyclerView = findViewById(R.id.recyclerView);
        query = db.collection(DBMeta.COLLECTION_TIMETABLE).whereEqualTo(DBMeta.DOCUMENT_TIMETABLE_DAY, day);

        mAdapter = new TimeTableAdapter(query);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}
