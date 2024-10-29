package com.example.happy_dream_app.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.happy_dream_app.R;
import android.content.Intent;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Navigate to LoginActivity
        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}