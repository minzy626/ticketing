package com.example.minji.ticketing;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    private static final String TAG = "Join in";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        mEmailField = (EditText) findViewById(R.id.et_joinemail);
        mPasswordField = (EditText) findViewById(R.id.et_joinpw);

        findViewById(R.id.bt_joinok).setOnClickListener(this);
        findViewById(R.id.bt_joinno).setOnClickListener(this);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(JoinActivity.this, "회원가입 성공",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MypageActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(JoinActivity.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("이메일을 입력해주세요");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("비밀번호를 입력해주세요");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.bt_joinok).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.bt_joinok).setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_joinok) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.bt_joinno) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
