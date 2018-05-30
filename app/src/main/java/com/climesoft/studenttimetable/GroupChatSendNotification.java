package com.climesoft.studenttimetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Group;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GroupChatSendNotification extends BaseBackActivity {

    private ArrayList<Subject> subjects = new ArrayList<>();
    private Spinner spinnerSubjects;
    private EditText editTextDate;
    private EditText editTextTime;
    private Button btnAdd;
    private TextInputLayout txtInputTopic;
    private DocumentReference msgDb;
    private Group group;
    private TextInputLayout txtNotifyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_send_notification);
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        btnAdd       = findViewById(R.id.btnAdd);
        txtInputTopic= findViewById(R.id.txtInputTopic);
        txtNotifyDetail = findViewById(R.id.txtNotifyDetail);
        if(getIntent() != null){
            group = getIntent().getParcelableExtra(KeyMeta.GROUP);
            msgDb = rootdb.collection(DBMeta.COLLECTION_GROUP).document(group.getId());
            setTitle(group.getName());
        }
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
                        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(GroupChatSendNotification.this, android.R.layout.simple_list_item_1, subjects);
                        spinnerSubjects.setAdapter(adapter);
                    }
                });
    }
    public void sendNotification(final View view){
        String title = txtInputTopic.getEditText().getText().toString();
        Subject objSubject = (Subject)spinnerSubjects.getSelectedItem();
        String subject = (objSubject).getName() + "-" +objSubject.getCode();
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String text = txtNotifyDetail.getEditText().getText().toString();

        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_MESSAGES_MEMBER, user.getUid());
        data.put(DBMeta.DOCUMENT_MESSAGES_TEXT, text);
        data.put(DBMeta.DOCUMENT_MESSAGES_IMPORTANT, true);
        data.put(DBMeta.DOCUMENT_MESSAGES_TIME, FieldValue.serverTimestamp());
        data.put(DBMeta.DOCUMENT_MESSAGES_DATE_TIME, date + "-" + time);
        data.put(DBMeta.DOCUMENT_MESSAGES_TITLE, title);
        data.put(DBMeta.DOCUMENT_MESSAGES_SUBJECT, subject);


        msgDb.collection(DBMeta.DOCUMENT_GROUP_MESSAGES)
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                ActivityUtil.showMessage(GroupChatSendNotification.this, "Notification Sent!");
                finish();
            }
        });

    }


    public void pickDate(View view){
        // Get Current Date
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void pickTime(View view){
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
}
