package com.climesoft.studenttimetable;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private TextInputLayout txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        txtEmail = findViewById(R.id.textInputEmail);
    }

    public void reset(final View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = txtEmail.getEditText().getText().toString();
        if(emailAddress.isEmpty()){
            ActivityUtil.showMessage(PasswordResetActivity.this, "Enter Valid Email");
            return;
        }
        view.setEnabled(false);
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ActivityUtil.showMessage(PasswordResetActivity.this, "Email Sent!");
                            txtEmail.getEditText().setText("");
                        }
                        view.setEnabled(true);
                    }
                });
    }

    public void registerActivity(View view) {
        ActivityUtil.moveToActivity(this, RegistrationActivity.class);
        this.finish();
    }
    public void login(View view) {
        ActivityUtil.moveToActivity(this, LoginActivity.class);
        this.finish();
    }
}
