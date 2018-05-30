package com.climesoft.studenttimetable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public abstract class BaseCompatActivity extends BaseActivity{

    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        if(user == null){
            ActivityUtil.moveToActivity(this, LoginActivity.class);
            this.finish();
        }
        setContentView(R.layout.activity_base);
        frameLayout = findViewById(R.id.content_frame);
        initToolbar();
        initNavigationDrawer();
//        setupFirestoreSetting();
    }

    private void setupFirestoreSetting() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        rootdb.setFirestoreSettings(settings);
    }

    protected void initToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }
    protected void initNavigationDrawer(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return manageNavigation(item.getItemId());
            }
        });
    }

    private boolean manageNavigation(int itemId) {
        switch(itemId){
            case R.id.nav_timetale:
                ActivityUtil.moveToActivity(BaseCompatActivity.this, TimeTableActivity.class);
                return true;
            case R.id.nav_subject:
                ActivityUtil.moveToActivity(BaseCompatActivity.this, SubjectActivity.class);
                return true;
            case R.id.nav_assignment:
                ActivityUtil.moveToActivity(BaseCompatActivity.this, AssignmentActivity.class);
                return true;
            case R.id.nav_quiz:
                ActivityUtil.moveToActivity(BaseCompatActivity.this, QuizActivity.class);
                return true;
            case R.id.nav_chat_group:
                ActivityUtil.moveToActivity(BaseCompatActivity.this, GroupActivity.class);
                return true;
            case R.id.nav_setting:
                ActivityUtil.moveToActivity(BaseCompatActivity.this, SettingActivity.class);
                return true;
            case R.id.nav_Logout:
                logout();
                return true;

        }
        return false;
    }

    protected void floatingButtonAction(final Class<?> desActivity){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.moveToActivity(BaseCompatActivity.this, desActivity);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

}
