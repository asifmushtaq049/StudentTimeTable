package com.climesoft.studenttimetable;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.model.TimeTable;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeTableAddActivity extends BaseBackActivity {

    private String day;
    private TextView txtDay;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private Spinner spinnerSubjects;
    private EditText editTextTime;
    private boolean isUpdate = false;
    private TimeTable timeTable;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_add);
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        editTextTime = findViewById(R.id.editTextTime);
        txtDay = findViewById(R.id.txtDay);
        btnAdd = findViewById(R.id.btnAdd);
        if(getIntent()!=null){
            if(getIntent().hasExtra(KeyMeta.DAY)){
                day = getIntent().getStringExtra(KeyMeta.DAY);
            }
            if(getIntent().hasExtra(KeyMeta.TIMETABLE)){
                timeTable = getIntent().getParcelableExtra(KeyMeta.TIMETABLE);
                isUpdate = true;
                editTextTime.setText(timeTable.getTime());
                btnAdd.setText("UPDATE");
            }
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

    public void addTimeTable(final View view) {
        view.setEnabled(false);
        final Subject subject = (Subject) spinnerSubjects.getSelectedItem();
        final DocumentReference subRef = db.collection(DBMeta.COLLECTION_SUBJECT).document(subject.getId());
        final String time = editTextTime.getText().toString();
        if(time.isEmpty()){
            ActivityUtil.showMessage(this, "Fill all fields!");
            view.setEnabled(true);
            return;
        }
        db.collection(DBMeta.COLLECTION_TIMETABLE)
                .whereEqualTo(DBMeta.DOCUMENT_TIMETABLE_SUBJECT, subRef)
                .whereEqualTo(DBMeta.DOCUMENT_TIMETABLE_DAY, day)
                .whereEqualTo(DBMeta.DOCUMENT_TIMETABLE_TIME, time)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        view.setEnabled(true);
                        if(queryDocumentSnapshots.isEmpty()){
                            if(isUpdate){
                                updateTimeTable(subRef, time, view);
                            }else{
                                addNewTimeTable(subRef, time, view);
                            }
                        }else{
                            ActivityUtil.showMessage(TimeTableAddActivity.this, "Already Exists!");
                        }
                    }
                });
    }

    private void addNewTimeTable(DocumentReference subRef, final String time, View view){
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_TIMETABLE_DAY, day);
        data.put(DBMeta.DOCUMENT_TIMETABLE_SUBJECT, subRef);
        data.put(DBMeta.DOCUMENT_TIMETABLE_TIME, time);
        DocumentReference doc = db.collection(DBMeta.COLLECTION_TIMETABLE).document();
        doc.set(data);
        ActivityUtil.showMessage(TimeTableAddActivity.this, "TimeTable Added!");
        createNotification(time, doc.getId().hashCode());
        TimeTableAddActivity.this.finish();
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        ActivityUtil.showMessage(TimeTableAddActivity.this, "TimeTable Added!");
//                        createNotification(time, documentReference.getId().hashCode());
//                        TimeTableAddActivity.this.finish();
//                    }
//                });
    }

    private void updateTimeTable(DocumentReference subRef, final String time, View view){
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_TIMETABLE_DAY, day);
        data.put(DBMeta.DOCUMENT_TIMETABLE_SUBJECT, subRef);
        data.put(DBMeta.DOCUMENT_TIMETABLE_TIME, time);
        db.collection(DBMeta.COLLECTION_TIMETABLE)
                .document(timeTable.getId())
                .set(data);
        ActivityUtil.showMessage(TimeTableAddActivity.this, "TimeTable Updated!");
        createNotification(time, timeTable.getId().hashCode());
        TimeTableAddActivity.this.finish();
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        ActivityUtil.showMessage(TimeTableAddActivity.this, "TimeTable Updated!");
//                        createNotification(time, timeTable.getId().hashCode());
//                        TimeTableAddActivity.this.finish();
//                    }
//                });
    }

    private void createNotification(String time, int hashCode) {
        String[] completeTime = time.split(":");

        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,ActivityUtil.dayToInt(day));
        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(completeTime[0]));
        cal.set(Calendar.MINUTE,Integer.parseInt(completeTime[1]));
        cal.set(Calendar.SECOND,0);

        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.TYPE, KeyMeta.TIMETABLE);
        bundle.putInt(KeyMeta.HASH_CODE, hashCode);
        bundle.putString(KeyMeta.SUBJECT, ((Subject)spinnerSubjects.getSelectedItem()).getName() + "-" + ((Subject)spinnerSubjects.getSelectedItem()).getCode());
        ActivityUtil.setTimeTableAlarm(this, cal, bundle, hashCode);
    }
}
