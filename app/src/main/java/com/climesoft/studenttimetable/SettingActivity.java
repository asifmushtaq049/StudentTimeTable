package com.climesoft.studenttimetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseBackActivity {

    private TextInputLayout txtName;
    private TextInputLayout txtTime;
    private TextInputLayout txtAssignment;
    private TextInputLayout txtQuiz;
    private TextInputLayout txtCustom;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        txtName = findViewById(R.id.txtName);
        txtTime = findViewById(R.id.txtTimeTable);
        txtAssignment = findViewById(R.id.txtAssignment);
        txtQuiz = findViewById(R.id.txtQuiz);
        txtCustom = findViewById(R.id.txtCustom);

        txtName.getEditText().setText(user.getDisplayName());
        prefs = getApplicationContext().getSharedPreferences(KeyMeta.PREF_SETTING, Context.MODE_PRIVATE);
        String time = prefs.getString(KeyMeta.PREF_SETTING_TIMETABLE, "0:15");
        String quiz = prefs.getString(KeyMeta.PREF_SETTING_QUIZ, "24:00");
        String assignment = prefs.getString(KeyMeta.PREF_SETTING_ASSIGNMENT, "24:00");
        String custom = prefs.getString(KeyMeta.PREF_SETTING_CUSTOM, "24:00");
        txtTime.getEditText().setText(time);
        txtQuiz.getEditText().setText(quiz);
        txtAssignment.getEditText().setText(assignment);
        txtCustom.getEditText().setText(custom);
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
        prefs.edit().putString(KeyMeta.PREF_SETTING_TIMETABLE, txtTime.getEditText().getText().toString()).apply();
        prefs.edit().putString(KeyMeta.PREF_SETTING_QUIZ, txtQuiz.getEditText().getText().toString()).apply();
        prefs.edit().putString(KeyMeta.PREF_SETTING_ASSIGNMENT, txtAssignment.getEditText().getText().toString()).apply();
        prefs.edit().putString(KeyMeta.PREF_SETTING_CUSTOM, txtCustom.getEditText().getText().toString()).apply();
        ActivityUtil.showMessage(this, "Setting Updated!");
        finish();
    }
}
