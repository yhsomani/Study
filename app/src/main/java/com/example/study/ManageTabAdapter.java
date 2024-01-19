// ManageTabAdapter.java
package com.example.study;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ManageTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    private int totalTabs;

    // Constructor to initialize the context and totalTabs
    public ManageTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // Return the fragment for each position
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Create and return a new instance of LoginFragment for the first tab
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;
            case 1:
                // Create and return a new instance of RegisterFragment for the second tab
                RegisterFragment registerFragment = new RegisterFragment();
                return registerFragment;
            default:
                // Return null for unknown position
                return null;
        }
    }

    // Return the total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
