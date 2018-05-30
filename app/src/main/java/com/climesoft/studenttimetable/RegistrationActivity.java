package com.climesoft.studenttimetable;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends BaseActivity {

    private static final String TAG = "Registration";
    private TextInputLayout txtEmail;
    private TextInputLayout txtPass;
    private TextInputLayout txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtEmail = findViewById(R.id.textInputEmail);
        txtPass = findViewById(R.id.textInputPassword);
        txtName = findViewById(R.id.textInputName);
    }

    public void loginActivity(View view) {
        ActivityUtil.moveToActivity(this, LoginActivity.class);
        this.finish();
    }
    public void register(final View view){
        String email = txtEmail.getEditText().getText().toString();
        String password = txtPass.getEditText().getText().toString();
        final String name = txtName.getEditText().getText().toString();
        if(email.isEmpty() || password.isEmpty() || name.isEmpty()){
            ActivityUtil.showMessage(this, "Fill all fields!");
            return;
        }
        view.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final Bundle bundle = new Bundle();
                            bundle.putString(KeyMeta.USER_NAME, name);
                            ActivityUtil.moveToActivity(RegistrationActivity.this, TimeTableActivity.class, bundle);
                            RegistrationActivity.this.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            ActivityUtil.showMessage(RegistrationActivity.this, "Something went wrong!");
                        }
                        view.setEnabled(true);
                    }
                });
    }
}
