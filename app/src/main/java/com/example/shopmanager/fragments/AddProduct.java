package com.example.shopmanager.fragments;

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

import org.jetbrains.annotations.NotNull;

public class AddProduct extends Fragment {
    private EditText nameField;
    private EditText idField;
    private EditText shelfField;
    private EditText quantityField;
    private TextView notification;

    public void addProduct(View view) {
        //Async Task to add product
        new AddProductTask(notification, this.getResources()).execute(new Product(
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
    }
}