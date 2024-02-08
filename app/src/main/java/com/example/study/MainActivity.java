// MainActivity.java
package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.study.whiteboard.WhiteBoardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit(); // Use commitNow() if immediate execution is desired
    }

    // Navigation item selection listener
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    String title = "";

                    switch (item.getItemId()) {
                        case R.id.QR_Code:
                            // TODO: Add QR code fragment
                            title = "White Board";
                            fragment = WhiteBoardFragment.newInstance("", "");
                            break;
                        case R.id.Chat:
                            title = "Chat";
                            fragment = ChatFragment.newInstance("", "");
                            break;
                        case R.id.Notice:
                            title = "NoticeBoard";
                            fragment = NoticeFragment.newInstance("", "");
                            break;
                        case R.id.Account:
                            title = "Account";
                            fragment = AccountFragment.newInstance("", "");
                            break;
                        default:
                            title = "Meet";
                            fragment = MeetFragment.newInstance("", "");
                            break;
                    }

                    // Set action bar title
                    getSupportActionBar().setTitle(title);

                    // Open the selected fragment
                    if (fragment != null) {
                        openFragment(fragment);
                        return true;
                    }
                    return false;
                }
            };
}
