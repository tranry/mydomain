package com.example.mydomain.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mydomain.fragment.AccountFragment;
import com.example.mydomain.fragment.CheckdomainFragment;
import com.example.mydomain.fragment.StoreFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StoreFragment();
            case 1:
                return new CheckdomainFragment();
            case 2:
                return new AccountFragment();
            default:
                return new StoreFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
