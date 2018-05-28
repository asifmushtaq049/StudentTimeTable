package com.climesoft.studenttimetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Assignment;
import com.climesoft.studenttimetable.model.Quiz;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class QuizAddActivity extends BaseBackActivity {

    private ArrayList<Subject> subjects = new ArrayList<>();
    private Spinner spinnerSubjects;
    private EditText editTextDate;
    private EditText editTextTime;
    private Button btnAdd;
    private TextInputLayout txtInputTopic;
    private Quiz quiz;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_add);
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        btnAdd       = findViewById(R.id.btnAdd);
        txtInputTopic= findViewById(R.id.txtInputTopic);

        if(getIntent().hasExtra(KeyMeta.QUIZ)){
            quiz = getIntent().getParcelableExtra(KeyMeta.QUIZ);
            isUpdate = true;
            txtInputTopic.getEditText().setText(quiz.getTopic());
            editTextDate.setText(quiz.getDate());
            editTextTime.setText(quiz.getTime());
            btnAdd.setText("Update");
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
                        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(QuizAddActivity.this, android.R.layout.simple_list_item_1, subjects);
                        spinnerSubjects.setAdapter(adapter);
                    }
                });
    }
    public void addQuiz(final View view){
        view.setEnabled(false);
        final String topic = txtInputTopic.getEditText().getText().toString().toUpperCase();
        final String date  = editTextDate.getText().toString().toUpperCase();
        final String time  = editTextTime.getText().toString().toUpperCase();
        final Subject subject = (Subject) spinnerSubjects.getSelectedItem();
        final DocumentReference subRef = db.collection(DBMeta.COLLECTION_SUBJECT).document(subject.getId());

        if(topic.isEmpty() || date.isEmpty() || time.isEmpty()){
            ActivityUtil.showMessage(this, "Fill All Fields!");
            view.setEnabled(true);
            return;
        }

        db.collection(DBMeta.COLLECTION_QUIZ)
                .whereEqualTo(DBMeta.DOCUMENT_QUIZ_TOPIC, topic)
                .whereEqualTo(DBMeta.DOCUMENT_QUIZ_SUBJECT, subRef)
                .whereEqualTo(DBMeta.DOCUMENT_QUIZ_DATE, date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        view.setEnabled(false);
                        if(queryDocumentSnapshots.isEmpty()){
                            if(isUpdate){
                                updateQuiz(topic, date, time, subRef, view);
                            }else{
                                addNewQuiz(topic, date, time, subRef, view);
                            }
                        }else{
                            ActivityUtil.showMessage(QuizAddActivity.this, "Already Exists!");
                        }
                    }
                });
    }

    private void addNewQuiz(String topic, String date, String time,
                                  DocumentReference subRef, final View view) {
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_QUIZ_TOPIC, topic);
        data.put(DBMeta.DOCUMENT_QUIZ_DATE, date);
        data.put(DBMeta.DOCUMENT_QUIZ_TIME, time);
        data.put(DBMeta.DOCUMENT_QUIZ_SUBJECT, subRef);
        db.collection(DBMeta.COLLECTION_QUIZ)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        ActivityUtil.showMessage(QuizAddActivity.this, "Quiz Added!");
                        view.setEnabled(true);
                        QuizAddActivity.this.finish();
                    }
                });
    }

    private void updateQuiz(String topic, String date, String time,
                                  DocumentReference subRef, final View view) {
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_QUIZ_TOPIC, topic);
        data.put(DBMeta.DOCUMENT_QUIZ_DATE, date);
        data.put(DBMeta.DOCUMENT_QUIZ_TIME, time);
        data.put(DBMeta.DOCUMENT_QUIZ_SUBJECT, subRef);
        db.collection(DBMeta.COLLECTION_QUIZ).document(quiz.getId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ActivityUtil.showMessage(QuizAddActivity.this, "Quiz Updated!");
                        view.setEnabled(true);
                        QuizAddActivity.this.finish();
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
