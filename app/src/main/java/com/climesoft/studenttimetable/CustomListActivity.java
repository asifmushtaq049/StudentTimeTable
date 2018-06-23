package com.climesoft.studenttimetable;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.climesoft.studenttimetable.adapters.CustomListAdapter;
import com.climesoft.studenttimetable.adapters.QuizAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.google.firebase.firestore.Query;

public class CustomListActivity extends BaseCompatActivity {

    private RecyclerView recyclerView;
    private CustomListAdapter mAdapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_custom_list, frameLayout, false);
        frameLayout.addView(coordinatorLayout);

        floatingButtonAction(CustomListAddActivity.class);


        recyclerView = findViewById(R.id.recyclerView);
        query = db.collection(DBMeta.COLLECTION_CUSTOM)
                .orderBy(DBMeta.DOCUMENT_CUSTOM_DATE)
                .orderBy(DBMeta.DOCUMENT_CUSTOM_TIME);

        mAdapter = new CustomListAdapter(query);
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
