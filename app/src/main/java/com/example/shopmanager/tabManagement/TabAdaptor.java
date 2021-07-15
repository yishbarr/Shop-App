package com.example.shopmanager.tabManagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shopmanager.fragments.AddProduct;
import com.example.shopmanager.fragments.ProductList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabAdaptor extends FragmentStateAdapter {
    private final List<Fragment> fragments;

    public TabAdaptor(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new ArrayList<>();
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
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public List<Fragment> getFragments() {
        return fragments;
    }
}
