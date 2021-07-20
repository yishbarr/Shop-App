package com.example.shopmanager.fragments;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shopmanager.R;
import com.example.shopmanager.asyncTasks.AddProductTask;
import com.example.shopmanager.models.Product;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddProduct extends Fragment {
    private EditText nameField;
    private EditText idField;
    private EditText shelfField;
    private EditText quantityField;
    private TextView notification;
    private FirebaseAuth mAuth;

    public void addProduct(View view) {
        notification.setVisibility(View.GONE);
        if (nameField.getText().toString().length() == 0 ||
                idField.getText().toString().length() == 0 ||
                shelfField.getText().toString().length() == 0 ||
                quantityField.getText().toString().length() == 0) {
            notification.setText(R.string.add_product_empty_field);
            notification.setVisibility(View.VISIBLE);
            notification.setTextColor(getResources().getColor(R.color.failure));
            return;
        }
        //Async Task to add product
        new AddProductTask(notification, this.getResources(), Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Product(
                        idField.getText().toString(),
                        nameField.getText().toString(),
                        Integer.parseInt(quantityField.getText().toString()),
                        Integer.parseInt(shelfField.getText().toString())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameField = view.findViewById(R.id.productNameField);
        idField = view.findViewById(R.id.productIdField);
        shelfField = view.findViewById(R.id.productShelfField);
        quantityField = view.findViewById(R.id.productQuantityField);
        notification = view.findViewById(R.id.productNotificationText);

        mAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}