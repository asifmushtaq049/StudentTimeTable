package com.climesoft.studenttimetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseBackActivity {

    private TextInputLayout txtName;
    private TextInputLayout txtTime;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        txtName = findViewById(R.id.txtName);
        txtTime = findViewById(R.id.textInputLayout2);
        txtName.getEditText().setText(user.getDisplayName());
        prefs = getApplicationContext().getSharedPreferences(KeyMeta.PREF_SETTING, Context.MODE_PRIVATE);
        String time = prefs.getString(KeyMeta.PREF_SETTING_TIME, "0:15");
        txtTime.getEditText().setText(time);
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
        prefs.edit().putString(KeyMeta.PREF_SETTING_TIME, txtTime.getEditText().getText().toString()).apply();
        ActivityUtil.showMessage(this, "Setting Updated!");
        finish();
    }
}
