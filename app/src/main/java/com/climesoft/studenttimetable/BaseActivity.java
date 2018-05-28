package com.climesoft.studenttimetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

abstract public class BaseActivity extends AppCompatActivity {

    protected FirebaseFirestore rootdb = FirebaseFirestore.getInstance();
    protected FirebaseAuth mAuth;
    protected FirebaseUser user;
    protected DocumentReference db;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = rootdb.collection(DBMeta.COLLECTION_USER).document(user.getUid());
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
