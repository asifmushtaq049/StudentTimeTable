package com.climesoft.studenttimetable;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.climesoft.studenttimetable.adapters.AssignmentAdapter;
import com.climesoft.studenttimetable.adapters.SubjectsAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.google.firebase.firestore.Query;

public class AssignmentActivity extends BaseCompatActivity{

    private RecyclerView recyclerView;
    private AssignmentAdapter mAdapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_assignment, frameLayout, false);
        frameLayout.addView(coordinatorLayout);
        floatingButtonAction(AssignmentAddActivity.class);

        recyclerView = findViewById(R.id.recyclerView);
        query = db.collection(DBMeta.COLLECTION_ASSIGNMENT)
                    .orderBy(DBMeta.DOCUMENT_ASSIGNMENT_DATE)
                    .orderBy(DBMeta.DOCUMENT_ASSIGNMENT_TIME);

        mAdapter = new AssignmentAdapter(query);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
