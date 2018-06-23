package com.climesoft.studenttimetable;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class GroupAddActivity extends BaseBackActivity {

    private TextInputLayout txtGroupName;
    private Subject subject;
    private boolean isUpdate = false;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        txtGroupName = findViewById(R.id.txtGroupName);

        if(getIntent().hasExtra(KeyMeta.GROUP)){
            subject = getIntent().getParcelableExtra(KeyMeta.SUBJECT);
            isUpdate = true;
            txtGroupName.getEditText().setText(subject.getName());
            btn.setText("Update!");
        }
    }

    public void addGroup(View view) {
        String userId = user.getUid();
        String groupName = txtGroupName.getEditText().getText().toString();
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_GROUP_ADMIN, userId);
        data.put(DBMeta.DOCUMENT_GROUP_NAME, groupName);
//        Map<String, Object> members = new HashMap<>();
//        members.put(DBMeta.DOCUMENT_GROUP_MEMBERS, member);
        final Map<String, Object> member = new HashMap<>();
        member.put(userId, true);
        data.put(DBMeta.DOCUMENT_GROUP_MEMBERS, member);
        rootdb.collection(DBMeta.COLLECTION_GROUP).add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        ActivityUtil.showMessage(GroupAddActivity.this, "Group Created!");
                        GroupAddActivity.this.finish();
                    }
                });
    }
}
