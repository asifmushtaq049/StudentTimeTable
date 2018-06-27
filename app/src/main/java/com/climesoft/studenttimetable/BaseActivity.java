package com.climesoft.studenttimetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
        if(checkPlayServices()) {
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            if (user != null) {
                db = rootdb.collection(DBMeta.COLLECTION_USER).document(user.getUid());
            }
        }
    }
    protected void logout(){
        if(mAuth!=null){
            mAuth.signOut();
            ActivityUtil.moveToActivity(this, LoginActivity.class);
            this.finish();
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }
}
