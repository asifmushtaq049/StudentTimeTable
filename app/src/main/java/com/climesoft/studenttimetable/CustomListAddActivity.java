package com.climesoft.studenttimetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.CustomList;
import com.climesoft.studenttimetable.model.Quiz;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomListAddActivity extends BaseBackActivity {

    private EditText editTextDate;
    private EditText editTextTime;
    private TextInputLayout txtInputTopic;
    private boolean isUpdate = false;
    private Button btnAdd;
    private CustomList custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_add);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        txtInputTopic= findViewById(R.id.txtInputTopic);
        btnAdd       = findViewById(R.id.btnAdd);
        if(getIntent().hasExtra(KeyMeta.CUSTOM)){
            custom = getIntent().getParcelableExtra(KeyMeta.CUSTOM);
            isUpdate = true;
            txtInputTopic.getEditText().setText(custom.getTopic());
            editTextDate.setText(custom.getDate());
            editTextTime.setText(custom.getTime());
            btnAdd.setText("Update");
        }
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

    public void addCustomList(final View view){
        view.setEnabled(false);
        final String topic = txtInputTopic.getEditText().getText().toString().toUpperCase();
        final String date  = editTextDate.getText().toString().toUpperCase();
        final String time  = editTextTime.getText().toString().toUpperCase();

        if(topic.isEmpty() || date.isEmpty() || time.isEmpty()){
            ActivityUtil.showMessage(this, "Fill All Fields!");
            view.setEnabled(true);
            return;
        }

        db.collection(DBMeta.COLLECTION_CUSTOM)
                .whereEqualTo(DBMeta.DOCUMENT_CUSTOM_TOPIC, topic)
                .whereEqualTo(DBMeta.DOCUMENT_CUSTOM_DATE, date)
                .whereEqualTo(DBMeta.DOCUMENT_CUSTOM_TIME, time)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        view.setEnabled(false);
                        if(queryDocumentSnapshots.isEmpty()){
                            if(isUpdate){
                                updateList(topic, date, time, view);
                            }else{
                                addNewList(topic, date, time, view);
                            }
                        }else{
                            ActivityUtil.showMessage(CustomListAddActivity.this, "Already Exists!");
                        }
                    }
                });
    }
    private void addNewList(String topic, final String date, final String time,final View view) {
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_CUSTOM_TOPIC, topic);
        data.put(DBMeta.DOCUMENT_CUSTOM_DATE, date);
        data.put(DBMeta.DOCUMENT_CUSTOM_TIME, time);
        DocumentReference doc = db.collection(DBMeta.COLLECTION_CUSTOM).document();
        doc.set(data);
        ActivityUtil.showMessage(CustomListAddActivity.this, "List Added!");
        view.setEnabled(true);
        createNotification(date, time, doc.getId().hashCode());
        CustomListAddActivity.this.finish();
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        ActivityUtil.showMessage(CustomListAddActivity.this, "List Added!");
//                        view.setEnabled(true);
//                        createNotification(date, time, documentReference.getId().hashCode());
//                        CustomListAddActivity.this.finish();
//                    }
//                });
    }
    private void createNotification(String date, String time, int hashCode) {
        String[] completeDate = date.split("-");
        String[] completeTime = time.split(":");

        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.MONTH,Integer.parseInt(completeDate[1]) -1);
        cal.set(Calendar.YEAR,Integer.parseInt(completeDate[2]));
        cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(completeDate[0]));
        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(completeTime[0]));
        cal.set(Calendar.MINUTE,Integer.parseInt(completeTime[1]));
        cal.set(Calendar.SECOND,0);

        Bundle bundle = new Bundle();
        bundle.putString(KeyMeta.TYPE, KeyMeta.CUSTOM);
        bundle.putInt(KeyMeta.HASH_CODE, hashCode);
        bundle.putString(KeyMeta.CUSTOM_TOPIC, txtInputTopic.getEditText().getText().toString());
        ActivityUtil.setAlarm(this, cal, bundle, hashCode);
    }
    private void updateList(String topic, final String date, final String time, final View view) {
        view.setEnabled(false);
        final Map<String, Object> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_CUSTOM_TOPIC, topic);
        data.put(DBMeta.DOCUMENT_CUSTOM_DATE, date);
        data.put(DBMeta.DOCUMENT_CUSTOM_TIME, time);

        db.collection(DBMeta.COLLECTION_CUSTOM).document(custom.getId())
                .set(data);
        ActivityUtil.showMessage(CustomListAddActivity.this, "Updated!");
        view.setEnabled(true);
        createNotification(date, time, custom.getId().hashCode());
        CustomListAddActivity.this.finish();
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        ActivityUtil.showMessage(CustomListAddActivity.this, "Updated!");
//                        view.setEnabled(true);
//                        createNotification(date, time, custom.getId().hashCode());
//                        CustomListAddActivity.this.finish();
//                    }
//                });
    }
}
