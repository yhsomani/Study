package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Duration of the splash screen in milliseconds (3 seconds)
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar
        getSupportActionBar().hide();

        // Load animations
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Get views from the layout
        ImageView logo = findViewById(R.id.logo);
        TextView slogan = findViewById(R.id.slogan);
        TextView name = findViewById(R.id.name);
        TextView own1 = findViewById(R.id.ownOne);
        TextView own2 = findViewById(R.id.ownTwo);

        // Set animations on views
        logo.setAnimation(topAnim);
        name.setAnimation(topAnim);
        slogan.setAnimation(topAnim);
        own1.setAnimation(bottomAnim);
        own2.setAnimation(bottomAnim);

        // Handle splash screen timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity and finish the splash activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
