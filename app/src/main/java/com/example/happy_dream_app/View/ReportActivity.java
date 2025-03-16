package com.example.happy_dream_app.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ReportDTO;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.ReportService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private EditText reportContent;
    private Button submitReportButton;
    private TextView address, addressDetail;
    private int selectedChargerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        selectedChargerId = getIntent().getIntExtra("chargerId", -1);
        String addr = getIntent().getStringExtra("address");
        String addrDetail = getIntent().getStringExtra("addressDetail");

        address = findViewById(R.id.review_address);
        addressDetail = findViewById(R.id.review_address_detail);
        reportContent = findViewById(R.id.report_content);
        submitReportButton = findViewById(R.id.btn_submit_report);

        submitReportButton.setOnClickListener(v -> submitReport());

        // 충전소 정보 표시
        address.setText(addr);
        addressDetail.setText(addrDetail);
    }

    private void submitReport() {
        String content = reportContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "고장 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ReportService 호출
        ReportService reportService = APIClient.getRetrofit().create(ReportService.class);

        ReportDTO reportRequest = new ReportDTO(selectedChargerId, content);

        reportService.submitReport(reportRequest).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReportActivity.this, "고장 신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ReportActivity.this, "고장 신고에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                Toast.makeText(ReportActivity.this, "서버 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
