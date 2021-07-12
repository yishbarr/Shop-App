package com.example.shopmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopmanager.asyncTasks.AddUser;
import com.example.shopmanager.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText userField;
    private EditText passwordField;
    private EditText shopField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userField = findViewById(R.id.userFieldReg);
        passwordField = findViewById(R.id.passwordFieldReg);
        shopField = findViewById(R.id.shopFieldReg);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        findViewById(R.id.registerAction).setEnabled(true);
                    } else {
                        Log.w("Anonymous Logon", "Failed");
                    }
                });
    }

    public void register(View view) {
        String email = userField.getText().toString();
        String password = passwordField.getText().toString();
        String shop = shopField.getText().toString();
        if (!(email.length() > 0 && password.length() > 0 && shop.length() > 0))
            return;
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent();
                        Log.d("Register", "Successful");
                        new AddUser().execute(new User(email, shop, mAuth.getCurrentUser().getUid()));
                        setResult(RESULT_OK, intent);
                        //Pass data to previous activity to log in and add to firebase.
                        intent.putExtra("EMAIL", email);
                        intent.putExtra("PASSWORD", password);
                        finish();
                    } else
                        Log.w("Register", "Failed");
                });
    }
}