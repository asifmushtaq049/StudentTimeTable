package com.climesoft.studenttimetable;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseBackActivity {

    private TextInputLayout txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        txtName = findViewById(R.id.txtName);
        txtName.getEditText().setText(user.getDisplayName());
    }

    public void updateSetting(View view) {
        String  name = txtName.getEditText().getText().toString();
        if(name.isEmpty()){
            return;
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates);
        final Map<String, String> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_USER_EMAIL, user.getEmail());
        data.put(DBMeta.DOCUMENT_USER_UID, user.getUid());
        data.put(DBMeta.DOCUMENT_USER_NAME, name);
        db.set(data);
        finish();
    }
}
