package com.example.happy_dream_app.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happy_dream_app.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName;
    private Button loginLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profile_name);
        loginLogoutButton = findViewById(R.id.btn_login);


        String username = getUsername();
        int userId = getUserId();

        // LoginActivity에서 저장된 username 받아오기
        if (isLoggedIn()) {
            profileName.setText(username);  // 유저 이름 표시
            loginLogoutButton.setText("로그아웃");  // 버튼 텍스트를 로그아웃으로 변경
        } else {
            profileName.setText("로그인을 해주세요.");
            loginLogoutButton.setText("로그인");
        }

        // 로그인/로그아웃 버튼 클릭 이벤트
        loginLogoutButton.setOnClickListener(view -> {
            if (isLoggedIn()) {
                logout();  // 로그아웃 수행
            } else {
                // 로그인 화면으로 이동
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // 로그인 여부 확인
    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getString("jwt_token", null) != null;
    }

    // 로그아웃 수행 (토큰 삭제 및 로그인 화면으로 이동)
    private void logout() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // 모든 데이터 삭제
        editor.commit();  // 즉시 저장
        Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();

        // 로그인 화면으로 이동
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    // userId 불러오기 함수
    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getInt("user_id", -1);  // 기본값 -1 반환
    }

    // username 불러오기 함수
    private String getUsername() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getString("username", "");  // 없을 경우 빈 문자열 반환
    }
}
