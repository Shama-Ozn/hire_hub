package com.example.hirehub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // After the delay, check if the user is logged in and navigate accordingly
            SharedPreferences sharedPref = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
            boolean isLoggedIn = sharedPref.getBoolean("IsLoggedIn", false);

            Intent intent;
            if (isLoggedIn) {
                // User is logged in, navigate to the main activity
                intent = new Intent(SplashActivity.this, FeedActivity.class);
            } else {
                // User is not logged in, navigate to the login activity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish(); // Make sure to call finish() so the user can't return to the splash screen by pressing the back button.
        }, 3000); // 3000 milliseconds = 3 seconds delay
    }
}
