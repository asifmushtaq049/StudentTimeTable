package com.climesoft.studenttimetable;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.climesoft.studenttimetable.adapters.SubjectsAdapter;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SubjectAddActivity extends BaseBackActivity {

    private Subject subject;
    private boolean isUpdate = false;
    private TextInputLayout subjectNameText;
    private TextInputLayout subjectCodeText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);
        subjectNameText = findViewById(R.id.act_subject_name);
        subjectCodeText = findViewById(R.id.act_subject_code);
        btn = findViewById(R.id.act_subject_add_btn);
        if(getIntent().hasExtra(KeyMeta.SUBJECT)){
            subject = getIntent().getParcelableExtra(KeyMeta.SUBJECT);
            isUpdate = true;
            subjectNameText.getEditText().setText(subject.getName());
            subjectCodeText.getEditText().setText(subject.getCode());
            btn.setText("Update!");
        }
    }

    public void addSubject(final View view){
        view.setEnabled(false);

        final String subjectName = subjectNameText.getEditText().getText().toString().toLowerCase();
        final String subjectCode = subjectCodeText.getEditText().getText().toString().toLowerCase();

        if(subjectCode.isEmpty() || subjectName.isEmpty()){
            ActivityUtil.showMessage(this, "Make sure to fill fields!");
            view.setEnabled(true);
            return;
        }

        db.collection(DBMeta.COLLECTION_SUBJECT)
                .whereEqualTo(DBMeta.DOCUMENT_SUBJECT_NAME, subjectName)
                .whereEqualTo(DBMeta.DOCUMENT_SUBJECT_CODE, subjectCode)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        view.setEnabled(true);
                        if(queryDocumentSnapshots.isEmpty()){
                            if(isUpdate){
                                updateSubject(subjectName, subjectCode, view);
                            }else{
                                addNewSubject(subjectName, subjectCode, view);
                            }
                        }else{
                            ActivityUtil.showMessage(SubjectAddActivity.this, "Already Exists!");
                        }
                    }
                });
    }

    private void addNewSubject(String subjectName, String subjectCode,
                               final View view) {
        view.setEnabled(false);
        final Map<String, String> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_SUBJECT_NAME, subjectName);
        data.put(DBMeta.DOCUMENT_SUBJECT_CODE, subjectCode);
        DocumentReference doc = db.collection(DBMeta.COLLECTION_SUBJECT).document();
        doc.set(data);
        ActivityUtil.showMessage(SubjectAddActivity.this, "Subject Added!");
        view.setEnabled(true);
        SubjectAddActivity.this.finish();

//        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
//                                @javax.annotation.Nullable FirebaseFirestoreException e) {
//
//            }
//        });

//        ActivityUtil.showMessage(SubjectAddActivity.this, "Subject Added!");



//                .onSuccessTask(new SuccessContinuation<DocumentReference, Object>() {
//                    @NonNull
//                    @Override
//                    public Task<Object> then(@Nullable DocumentReference documentReference) throws Exception {
//                        ActivityUtil.showMessage(SubjectAddActivity.this, "Subject Added!");
//                        view.setEnabled(true);
//                        SubjectAddActivity.this.finish();
//                        return null;
//                    }
//                });

//        if(doc != null){
//            ActivityUtil.showMessage(SubjectAddActivity.this, "Subject Added!");
//            view.setEnabled(true);
//            SubjectAddActivity.this.finish();
//        }
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        ActivityUtil.showMessage(SubjectAddActivity.this, "Subject Added!");
//                        view.setEnabled(true);
//                        SubjectAddActivity.this.finish();
//                    }
//                });
    }

    private void updateSubject(String subjectName, String subjectCode,
                               final View view) {
        view.setEnabled(false);
        final Map<String, String> data = new HashMap<>();
        data.put(DBMeta.DOCUMENT_SUBJECT_NAME, subjectName);
        data.put(DBMeta.DOCUMENT_SUBJECT_CODE, subjectCode);
        DocumentReference doc = db.collection(DBMeta.COLLECTION_SUBJECT).document(subject.getId());
        doc.set(data);
        ActivityUtil.showMessage(SubjectAddActivity.this, "Subject Updated!");
        view.setEnabled(true);
        SubjectAddActivity.this.finish();
    }
}
