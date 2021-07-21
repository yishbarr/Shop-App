package com.example.shopmanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopmanager.R;
import com.example.shopmanager.asyncTasks.UpdateProductTask;
import com.example.shopmanager.models.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class EditProduct extends AppCompatActivity {
    private EditText nameField;
    private EditText shelfField;
    private EditText quantityField;
    private String id;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        nameField = findViewById(R.id.productNameField);
        shelfField = findViewById(R.id.productShelfField);
        quantityField = findViewById(R.id.productQuantityField);
        notification = findViewById(R.id.productNotificationText);

        Intent intent = getIntent();
        nameField.setText(intent.getStringExtra("name"));
        shelfField.setText(intent.getStringExtra("shelf"));
        quantityField.setText(intent.getStringExtra("quantity"));
        id = intent.getStringExtra("id");
    }

    public void updateProduct(View view) {
        String name = nameField.getText().toString();
        int shelf = Integer.parseInt(shelfField.getText().toString());
        int quantity = Integer.parseInt(quantityField.getText().toString());
        if (name.length() == 0 || (shelf + "").length() == 0 || (quantity + "").length() == 0)
            return;
        new UpdateProductTask(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Product(id, name, quantity, shelf));
    }
}