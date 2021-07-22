package com.example.shopmanager.fragments;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shopmanager.R;
import com.example.shopmanager.asyncTasks.GetProductsTask;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProductList extends Fragment {
    private TableLayout productsTable;
    private TableRow productsHeader;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        productsTable = view.findViewById(R.id.productsTable);
        productsHeader = view.findViewById(R.id.productsDetails);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onResume() {
        productsTable.removeAllViews();
        productsTable.addView(productsHeader);
        super.onResume();
        new GetProductsTask(getContext(),
                productsTable,
                getResources(),
                Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}