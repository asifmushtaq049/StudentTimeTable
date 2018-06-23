package com.climesoft.studenttimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.climesoft.studenttimetable.adapters.GroupAdapter;
import com.climesoft.studenttimetable.adapters.MembersAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Group;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class MembersActivity extends BaseBackActivity  {

    private RecyclerView recyclerView;
    private MembersAdapter mAdapter;
    private DocumentReference groupRef;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        if(getIntent().hasExtra(KeyMeta.GROUP)){
            group = getIntent().getParcelableExtra(KeyMeta.GROUP);
            setTitle(group.getName());
            recyclerView = findViewById(R.id.recyclerView);
            groupRef = rootdb.collection(DBMeta.COLLECTION_GROUP).document(group.getId());
            groupRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    final Map<String, Boolean> map = (Map<String, Boolean>) documentSnapshot.get(DBMeta.DOCUMENT_GROUP_MEMBERS);
                    final ArrayList<String> members = new ArrayList<>(map.keySet());
                    mAdapter = new MembersAdapter(members, rootdb, group);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MembersActivity.this));
                }
            });
        }
    }
}
