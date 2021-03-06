package com.example.shopmanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopmanager.R;
import com.example.shopmanager.asyncTasks.AddUserTask;
import com.example.shopmanager.constants.LogTags;
import com.example.shopmanager.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private final String REGISTER_TAG = LogTags.REGISTER.toString();
    private FirebaseAuth mAuth;
    private EditText userField;
    private EditText passwordField;
    private EditText shopField;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userField = findViewById(R.id.userFieldReg);
        passwordField = findViewById(R.id.passwordFieldReg);
        shopField = findViewById(R.id.shopFieldReg);
        notification = findViewById(R.id.registerNotification);

        mAuth = FirebaseAuth.getInstance();
    }

    //Register user with input.
    public void register(View view) {
        String email = userField.getText().toString();
        String password = passwordField.getText().toString();
        String shop = shopField.getText().toString();
        if (!(email.length() > 0 && password.length() > 0 && shop.length() > 0)) {
            notification.setVisibility(View.VISIBLE);
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent();
                        Log.d(REGISTER_TAG, "Successful");
                        new AddUserTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new User(email, shop, Objects.requireNonNull(mAuth.getCurrentUser()).getUid()));
                        setResult(RESULT_OK, intent);
                        //Pass data to previous activity to log in and add to firebase.
                        intent.putExtra("EMAIL", email);
                        intent.putExtra("PASSWORD", password);
                        finish();
                    } else {
                        Log.w(REGISTER_TAG, "Failed");
                        notification.setVisibility(View.VISIBLE);
                    }
                });
    }
}