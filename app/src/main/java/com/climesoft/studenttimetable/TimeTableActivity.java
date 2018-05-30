package com.climesoft.studenttimetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class TimeTableActivity extends BaseCompatActivity {
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_time_table, null);
        frameLayout.addView(linearLayout);
        if(getIntent() != null){
            if(getIntent().hasExtra(KeyMeta.USER_NAME)){
                name = getIntent().getStringExtra(KeyMeta.USER_NAME);
                attachName(name);
                updateUserData();
            }else{
                db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        attachName(documentSnapshot.getString(DBMeta.DOCUMENT_USER_NAME));
                    }
                });
            }
        }
    }

    private void attachName(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates);
    }

    private void updateUserData() {
        final Map<String, String> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_USER_EMAIL, user.getEmail());
        data.put(DBMeta.DOCUMENT_USER_UID, user.getUid());
        data.put(DBMeta.DOCUMENT_USER_NAME, name);
        db.set(data);
//        ActivityUtil.showMessage(this, user.getDisplayName());
    }

    public void mondayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Monday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }
    public void tuesdayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Tuesday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }
    public void wednesdayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Wednesday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }
    public void thursdayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Thursday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }
    public void fridayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Friday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }
    public void saturdayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Saturday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }
    public void sundayTimeTable(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.DAY, "Sunday");
        ActivityUtil.moveToActivity(this, TimeTableDetailActivity.class, bundle);
    }

}
