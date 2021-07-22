package com.example.shopmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopmanager.R;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.constants.RequestCodes;
import com.google.firebase.auth.FirebaseAuth;


public class Logon extends AppCompatActivity {
    private final String LOGON_TAG = LogTags.LOGON.toString();
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.userField);
        passwordField = findViewById(R.id.passwordField);
        notification=findViewById(R.id.notification);
    }

    //Login with user input
    public void validate(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (email.length() > 0 && password.length() > 0)
            logon(email, password);
        else notification.setVisibility(View.VISIBLE);
    }

    //Login with credentials
    public void logon(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOGON_TAG, "Successful");
                        notification.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        Log.w(LOGON_TAG, "Failed");
                        notification.setVisibility(View.VISIBLE);
                    }
                });
    }

    //Screen to register new account.
    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivityForResult(intent, RequestCodes.REGISTER.getCode());
    }

    //When returning from another activity activated by this one.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.REGISTER.getCode()) {
            //Login with new account.
            if (resultCode == RESULT_OK && data != null) {
                String email = data.getStringExtra("EMAIL");
                logon(email,
                        data.getStringExtra("PASSWORD"));

            }
        }
    }
}