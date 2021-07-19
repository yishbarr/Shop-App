package com.example.shopmanager.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shopmanager.R;
import com.example.shopmanager.fragments.AddProduct;
import com.example.shopmanager.tabManagement.TabAdaptor;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TabAdaptor tabAdaptor;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Class to manage tabs
        tabAdaptor = new TabAdaptor(this);
        viewPager = findViewById(R.id.viewPager);
        //Attach the tab manager to the View Pager.
        viewPager.setAdapter(tabAdaptor);

        //Add all the tabs to the tab layout.
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            String tabName;
            switch (position) {
                case 0:
                    tabName = "Product List";
                    break;
                case 1:
                    tabName = "Add Product";
                    break;
                default:
                    tabName = "Tab";
            }
            tab.setText(tabName);
        }).attach();
    }

    //Function accessible from fragment.
    public void addProduct(View view) {
        ((AddProduct) getSupportFragmentManager().findFragmentByTag("f1")).addProduct(view);
    }

}