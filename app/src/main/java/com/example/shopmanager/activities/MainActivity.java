package com.example.shopmanager.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shopmanager.R;
import com.example.shopmanager.fragments.AddProduct;
import com.example.shopmanager.tabManagement.TabAdaptor;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabAdaptor tabAdaptor;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabAdaptor = new TabAdaptor(this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(tabAdaptor);

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

    public void addProduct(View view){
        AddProduct.addProduct(view);
    }

}