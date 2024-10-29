package com.example.happy_dream_app.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.DTO.UserDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.SignupService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnSignup;
    private SignupService signupService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // UI 요소 초기화
        etUsername = findViewById(R.id.et_signup_username);
        etPassword = findViewById(R.id.et_signup_password);
        etConfirmPassword = findViewById(R.id.et_signup_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);

        // SignupService 인스턴스 생성
        signupService = APIClient.getRetrofit().create(SignupService.class);

        // 회원가입 버튼 클릭 이벤트
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                signup(username, password);
            }
        });
    }

    // 회원가입 로직
    private void signup(String username, String password) {
        UserDTO userDTO = new UserDTO(username, password);
        signupService.signup(username, userDTO).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    finish();  // 로그인 화면으로 복귀
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
