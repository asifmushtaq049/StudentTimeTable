package com.climesoft.studenttimetable;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class GroupAddActivity extends BaseBackActivity {

    private TextInputLayout txtGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        txtGroupName = findViewById(R.id.txtGroupName);
    }

    public void addGroup(View view) {
        String userId = user.getUid();
        String groupName = txtGroupName.getEditText().getText().toString();
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_GROUP_ADMIN, userId);
        data.put(DBMeta.DOCUMENT_GROUP_NAME, groupName);
        data.put(userId, true);
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
