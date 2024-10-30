package com.example.happy_dream_app.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ReviewDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.ReviewService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private int chargerId;
    private int userId;

    private TextView address, addressDetail;
    private EditText reviewContent;
    private RatingBar reviewRating;
    private Button btnSubmitReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // 인텐트로부터 데이터 받기
        chargerId = getIntent().getIntExtra("chargerId", -1);
        String addr = getIntent().getStringExtra("address");
        String addrDetail = getIntent().getStringExtra("addressDetail");

        // SharedPreferences에서 userId 불러오기
        userId = getUserIdFromPrefs();

        // UI 요소 초기화
        address = findViewById(R.id.review_address);
        addressDetail = findViewById(R.id.review_address_detail);
        reviewContent = findViewById(R.id.review_content);
        reviewRating = findViewById(R.id.review_rating);
        btnSubmitReview = findViewById(R.id.btn_submit_review);

        // 충전소 정보 표시
        address.setText(addr);
        addressDetail.setText(addrDetail);

        // 리뷰 추가 버튼 클릭 이벤트
        btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        String content = reviewContent.getText().toString();
        byte rating = (byte) reviewRating.getRating();

        if (content.isEmpty() || rating == 0) {
            Toast.makeText(this, "리뷰와 별점을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ReviewService 호출
        ReviewService reviewService = APIClient.getRetrofit().create(ReviewService.class);

        ReviewDTO review = new ReviewDTO(chargerId, userId, content, rating);

        // 경로 파라미터와 리뷰 본문, 별점 전달
        Call<Void> call = reviewService.addReview(chargerId, review);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "리뷰가 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();  // 액티비티 종료
                } else {
                    Toast.makeText(ReviewActivity.this, "리뷰 추가 실패: "
                            + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // SharedPreferences에서 userId 불러오기
    private int getUserIdFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getInt("user_id", -1);  // userId가 없으면 -1 반환
    }
}
