package com.example.study;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#31E373"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(MeetFragment.newInstance("", ""));
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Meet:
                    openFragment(MeetFragment.newInstance("", ""));
                    return true;
                case R.id.QR_Code:
//                    openFragment(QR_CodeFragment.newInstance("", ""));
                    return true;
//                case R.id.Examination:
//                    openFragment(ExaminationFragment.newInstance("", ""));
//                    return true;
                case R.id.Notice:
//                    openFragment(NoticeFragment.newInstance("", ""));
                    return true;
                case R.id.Account:
                    openFragment(AccountFragment.newInstance("", ""));
                    return true;
            }
            return false;
        }
    };
}