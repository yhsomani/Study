package com.example.study;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class ManageTabAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    public ManageTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LoginFragment homeFragment = new LoginFragment();
                return homeFragment;
            case 1:
                RegisterFragment sportFragment = new RegisterFragment();
                return sportFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}