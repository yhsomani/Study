// LoginActivity.java
package com.example.study;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Task 1: Hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Task 2: Initialize TabLayout and ViewPager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Task 3: Add tabs to the TabLayout ("Login" and "Register")
        addTabsToTabLayout();

        // Task 4: Create an adapter for managing tabs
        ManageTabAdapter adapter = createTabAdapter();

        // Task 5: Set up ViewPager to respond to page changes
        configureViewPager(adapter);

        // Task 6: Set up TabLayout to respond to tab selection
        configureTabLayout(adapter);
    }

    private void addTabsToTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.login_tab_title))); // Replace with your login tab title resource
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.register_tab_title))); // Replace with your register tab title resource
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private ManageTabAdapter createTabAdapter() {
        return new ManageTabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
    }

    private void configureViewPager(ManageTabAdapter adapter) {
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void configureTabLayout(final ManageTabAdapter adapter) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Task 7: Switch to the selected tab when a tab is selected
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Task 8: Handle unselected tab if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Task 9: Handle reselected tab if needed
            }
        });
    }
}
