package com.example.shopmanager.tabManagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shopmanager.fragments.AddProduct;
import com.example.shopmanager.fragments.ProductList;

import org.jetbrains.annotations.NotNull;

public class TabAdaptor extends FragmentStateAdapter {
    public TabAdaptor(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new AddProduct();
                break;
            default:
                fragment = new ProductList();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
