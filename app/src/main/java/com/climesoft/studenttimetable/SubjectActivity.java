package com.climesoft.studenttimetable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import com.climesoft.studenttimetable.adapters.SubjectsAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.google.firebase.firestore.Query;

public class SubjectActivity extends BaseCompatActivity{

    private RecyclerView subjects;
    private SubjectsAdapter mAdapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_subject, frameLayout, false);
        frameLayout.addView(coordinatorLayout);

        floatingButtonAction(SubjectAddActivity.class);

        subjects = findViewById(R.id.act_subject_recycler);
        query = db.collection(DBMeta.COLLECTION_SUBJECT);

        mAdapter = new SubjectsAdapter(query);
        subjects.setAdapter(mAdapter);
        subjects.setLayoutManager(new LinearLayoutManager(this));
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
