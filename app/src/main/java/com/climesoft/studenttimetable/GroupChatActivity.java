package com.climesoft.studenttimetable;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.climesoft.studenttimetable.adapters.GroupChatAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Group;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.climesoft.studenttimetable.util.DialogUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class GroupChatActivity extends BaseBackActivity {

    private Group group;
    private boolean isAdmin = false;
    private DocumentReference msgDb;
    private EditText txtMessage;
    private boolean isImportant = false;
    private RecyclerView mRecyclerView;
    private Query mQuery;
    private GroupChatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        txtMessage = findViewById(R.id.txtMessage);
        mRecyclerView = findViewById(R.id.msgRecycler);
        if(getIntent() != null){
            group = getIntent().getParcelableExtra(KeyMeta.GROUP);
            if(group.getAdmin().equals(user.getUid())){
                isAdmin = true;
            }
            msgDb = rootdb.collection(DBMeta.COLLECTION_GROUP).document(group.getId());
            setTitle(group.getName());
        }
        mQuery = msgDb.collection(DBMeta.DOCUMENT_GROUP_MESSAGES).orderBy(DBMeta.DOCUMENT_MESSAGES_TIME);
        mAdapter = new GroupChatAdapter(mQuery){
            @Override
            protected void onDataChanged(){
                scrollToEnd();
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToEnd();
            }
        }, 300);
    }

    private void scrollToEnd(){
        if(mAdapter.getItemCount() > 0){
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
        }
    }


    public void sendMessage(final View view){
        String msg = txtMessage.getText().toString();
        if(msg.isEmpty()){
            return;
        }
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_MESSAGES_MEMBER, user.getUid());
        data.put(DBMeta.DOCUMENT_MESSAGES_TEXT, msg);
        data.put(DBMeta.DOCUMENT_MESSAGES_IMPORTANT, isImportant);
        data.put(DBMeta.DOCUMENT_MESSAGES_TIME, FieldValue.serverTimestamp());
        msgDb.collection(DBMeta.DOCUMENT_GROUP_MESSAGES)
                .add(data);
        txtMessage.setText("");
        view.setEnabled(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
        scrollToEnd();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_member_add:
                addNewMember();
                return true;
            case R.id.item_member_leave:
                leaveMember();
                return true;
            case R.id.item_members:
                final Bundle groupBundle = new Bundle();
                groupBundle.putParcelable(KeyMeta.GROUP, group);
                ActivityUtil.moveToActivity(this, MembersActivity.class, groupBundle);
                return true;
            case R.id.item_notify:
                final Bundle bundle = new Bundle();
                bundle.putParcelable(KeyMeta.GROUP, group);
                ActivityUtil.moveToActivity(this, GroupChatSendNotification.class, bundle);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void leaveMember() {
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_GROUP_MEMBERS+"."+user.getUid(), FieldValue.delete());
        rootdb.collection(DBMeta.COLLECTION_GROUP).document(group.getId())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ActivityUtil.showMessage(GroupChatActivity.this, "Left the Group!");
                        finish();
                    }
                });
    }

    private void addNewMember() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == dialog.BUTTON_POSITIVE){
                    AlertDialog dialog1 = (AlertDialog) dialog;
                    final String email = ((TextInputLayout)dialog1.findViewById(R.id.dialog_add_userEmail)).getEditText().getText().toString();
                    rootdb.collection(DBMeta.COLLECTION_USER).whereEqualTo(DBMeta.DOCUMENT_USER_EMAIL, email).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            ActivityUtil.showMessage(GroupChatActivity.this, "No User found!");
                                            return;
                                        }
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            final String uid = document.getId();
                                            final Map<String, Object> member = new HashMap<>();
                                            member.put(uid, true);
                                            Map<String, Object> members = new HashMap<>();
                                            members.put(DBMeta.DOCUMENT_GROUP_MEMBERS, member);
                                            rootdb.collection(DBMeta.COLLECTION_GROUP).document(group.getId())
                                                    .set(members, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            ActivityUtil.showMessage(GroupChatActivity.this, "Member Added!");
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });

                }
            }
        };
        DialogUtil.showDialog(this,R.layout.dialog_add_member_email, "Add New Member","Add", "Cancel", listener, listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group_chat, menu);
        MenuItem addItem = menu.findItem(R.id.item_member_add);
        MenuItem leaveItem = menu.findItem(R.id.item_member_leave);
        MenuItem notifyItem = menu.findItem(R.id.item_notify);
        if(isAdmin){
            leaveItem.setVisible(false);
        }else {
            addItem.setVisible(false);
            notifyItem.setVisible(false);
        }
        return true;
    }
}
