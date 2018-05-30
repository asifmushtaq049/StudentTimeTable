package com.climesoft.studenttimetable;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.climesoft.studenttimetable.adapters.AssignmentAdapter;
import com.climesoft.studenttimetable.adapters.QuizAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.google.firebase.firestore.Query;

public class QuizActivity extends BaseCompatActivity{

    private RecyclerView recyclerView;
    private QuizAdapter mAdapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_quiz, frameLayout, false);
        frameLayout.addView(coordinatorLayout);

        floatingButtonAction(QuizAddActivity.class);

        recyclerView = findViewById(R.id.recyclerView);
        query = db.collection(DBMeta.COLLECTION_QUIZ)
                .orderBy(DBMeta.DOCUMENT_QUIZ_DATE)
                .orderBy(DBMeta.DOCUMENT_QUIZ_TIME);

        mAdapter = new QuizAdapter(query);
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
