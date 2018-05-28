package com.climesoft.studenttimetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

abstract public class BaseActivity extends AppCompatActivity {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected FirebaseAuth mAuth;
    protected FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .build();
//        db.setFirestoreSettings(settings);
    }
    protected void logout(){
        if(mAuth!=null){
            mAuth.signOut();
            ActivityUtil.moveToActivity(this, LoginActivity.class);
        }
    }
}
