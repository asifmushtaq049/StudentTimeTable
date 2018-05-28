package com.climesoft.studenttimetable;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeTableAddActivity extends BaseBackActivity {

    private String day;
    private TextView txtDay;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private Spinner spinnerSubjects;
    private EditText editTextTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_add);
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        editTextTime = findViewById(R.id.editTextTime);
        txtDay = findViewById(R.id.txtDay);
        if(getIntent()!=null){
            day = getIntent().getStringExtra(KeyMeta.DAY);
        }
        txtDay.setText(day);
        loadSubjects();
    }

    private void loadSubjects(){
        db.collection(DBMeta.COLLECTION_SUBJECT).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Subject subject = documentSnapshot.toObject(Subject.class);
                            subject.setId(documentSnapshot.getId());
                            subjects.add(subject);
                        }
                        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(TimeTableAddActivity.this, android.R.layout.simple_list_item_1, subjects);
                        spinnerSubjects.setAdapter(adapter);
                    }
                });
    }



    public void pickTime(View view) {
        // Get Current Time
        int mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void addTimeTable(View view) {

    }
}
