// MainActivity.java
package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

// Import statements...

public class MainActivity extends AppCompatActivity {

    // UI components
    private BottomNavigationView bottomNavigation;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Get BottomNavigationView reference and set the listener
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        // Open the default fragment when the activity is created
        openFragment(MeetFragment.newInstance("", ""));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is not authenticated, redirect to LoginActivity and finish MainActivity
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    // Open a fragment in the container
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit(); // Use commitNow() if immediate execution is desired
    }

    // Navigation item selection listener
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.QR_Code:
                            // TODO: Add QR code fragment
                            getSupportActionBar().setTitle("QR-Code Scanner");
                            break;
                        case R.id.Chat:
                            getSupportActionBar().setTitle("Chat");
                            fragment = ChatFragment.newInstance("", "");
                            break;
                        case R.id.Notice:
                            getSupportActionBar().setTitle("NoticeBoard");
                            fragment = NoticeFragment.newInstance("", "");
                            break;
                        case R.id.Account:
                            getSupportActionBar().setTitle("Account");
                            fragment = new AccountFragment(); // Updated instantiation
                            break;
                        default:
                            getSupportActionBar().setTitle("Meet");
                            fragment = MeetFragment.newInstance("", "");
                            break;
                    }
                    // Open the selected fragment
                    openFragment(fragment);
                    return true;
                }
            };
}
