package com.example.happy_dream_app.View;

import android.content.Intent;
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

                signup(username, password);
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

    private void signup(String username, String password) {
        UserDTO userDTO = new UserDTO(username, password);
        loginService.login(username, userDTO).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    finish();  // 로그인 화면으로 복귀
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
