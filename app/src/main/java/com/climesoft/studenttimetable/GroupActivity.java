package com.climesoft.studenttimetable;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.climesoft.studenttimetable.adapters.GroupAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.model.Group;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class GroupActivity extends BaseCompatActivity {

    private RecyclerView recyclerView;
    private GroupAdapter mAdapter;
    private Group group;
    private Query query;
    private ArrayList<Group> groups = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) LayoutInflater.from(this)
                        .inflate(R.layout.activity_group, frameLayout, false);
        frameLayout.addView(coordinatorLayout);
        recyclerView = findViewById(R.id.recyclerView);
        query = rootdb.collection(DBMeta.COLLECTION_GROUP).whereEqualTo(user.getUid(), true);
        mAdapter = new GroupAdapter(query);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatingButtonAction(GroupAddActivity.class);
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
