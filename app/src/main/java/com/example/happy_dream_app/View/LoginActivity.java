package com.example.happy_dream_app.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.DTO.UserDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.LoginService;
import com.example.happy_dream_app.Service.SignupService;
import com.example.happy_dream_app.View.MainActivity;
import com.example.happy_dream_app.View.SignupActivity;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        loginService = APIClient.getRetrofit().create(LoginService.class);

        // 로그인 버튼 클릭 이벤트
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                login(username, password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 회원가입 텍스트 클릭 시 회원가입 페이지로 이동
        findViewById(R.id.btn_signup_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        Button signupButton = findViewById(R.id.btn_signup_link);
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void login(String username, String password) {
        UserDTO userDTO = new UserDTO(username, password);
        loginService.login(username, userDTO).enqueue(new Callback<ResponseDTO>() {
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getMessage();    // 서버에서 받은 JWT 토큰

                    // 토큰 저장
                    saveToken(token);

                    // 토큰에서 username 추출 및 userID 추출 후 다음 화면으로 이동
                    String extractedUsername = extractUsernameFromToken(token);
                    saveUserId(extractUserIdFromToken(token));
                    saveUsername(extractUsernameFromToken(token));

                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    intent.putExtra("username", extractedUsername);
                    startActivity(intent);  // ProfileActivity로 이동

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    finish();  // LoginActivity 종료
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // JWT 토큰 저장 함수
    private void saveToken(String token) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt_token", token);
        editor.apply();  // 비동기 저장
    }

    // JWT 토큰 불러오기 함수 (다른 화면에서 사용 가능)
    private String getToken() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getString("jwt_token", null);  // 토큰이 없으면 null 반환
    }


    // userId 저장 함수
    private void saveUserId(int userId) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }

    // userId 불러오기 함수
    private String getUserId() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getString("user_id", "");  // 없을 경우 빈 문자열 반환
    }

    // username 저장 함수
    private void saveUsername(String username) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.apply();
    }

    // username 불러오기 함수
    private String getUsername() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getString("username", "");  // 없을 경우 빈 문자열 반환
    }


    // JWT 토큰에서 username 추출 (JWT의 payload 해석)
    private String extractUsernameFromToken(String token) {
        String[] parts = token.split("\\.");  // JWT 토큰은 3개의 부분으로 나뉨 (header.payload.signature)
        if (parts.length < 3) {
            throw new IllegalArgumentException("잘못된 JWT 토큰 형식입니다.");
        }

        // 두 번째 부분(payload) 디코딩
        String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));

        // JSON 파싱 후 "sub" 값 추출
        try {
            JSONObject json = new JSONObject(payload);
            return json.getString("sub");  // "sub" 키에 username이 있다고 가정
        } catch (IllegalArgumentException | JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(this, "토큰 해석 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            return "";  // 문제가 발생하면 빈 문자열 반환
        }
    }

    // JWT 토큰에서 userId 추출
    private int extractUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
            JSONObject json = new JSONObject(payload);
            return json.getInt("userId");  // userId 추출
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;  // 오류 시 기본값 반환
        }
    }
}
