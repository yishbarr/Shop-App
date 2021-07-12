package com.example.shopmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopmanager.constants.RequestCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Logon extends AppCompatActivity {
    private final String LogonTag = "Logon";
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.userField);
        passwordField = findViewById(R.id.passwordField);
    }

    public void validate(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (email.length() > 0 && password.length() > 0)
            logon(email, password);
    }

    public void logon(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(LogonTag, "Successful");
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        Log.w(LogonTag, "Failed");
                    }
                });
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivityForResult(intent, RequestCodes.register.getCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.register.getCode()) {
            if (resultCode == RESULT_OK && data != null) {
                String email = data.getStringExtra("EMAIL");
                logon(email,
                        data.getStringExtra("PASSWORD"));

            } else if (resultCode == RESULT_CANCELED) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null)
                    currentUser.delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                    Log.d("Register", "Anonymous account deleted");
                            });
            }
        }
    }
}