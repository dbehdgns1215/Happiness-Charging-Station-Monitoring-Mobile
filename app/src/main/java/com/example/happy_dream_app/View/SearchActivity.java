package com.example.happy_dream_app.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.NaverLocalSearchResponseDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.NaverLocalSearchService;
import com.example.happy_dream_app.Util.CoordinateConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchResultsAdapter.OnItemClickListener {

    private EditText etSearchQuery;
    private Button btnSearch;
    private RecyclerView rvSearchResults;
    private SearchResultsAdapter adapter;

    // 네이버 API 정보
    private static final String CLIENT_ID = "2xCA0FlaYN5geYnHyn_n"; // 네이버 클라이언트 ID
    private static final String CLIENT_SECRET = "Yvp6YMZBNX"; // 네이버 클라이언트 시크릿

    private static final int SEARCH_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearchQuery = findViewById(R.id.et_search_query);
        btnSearch = findViewById(R.id.btn_search);
        rvSearchResults = findViewById(R.id.rv_search_results);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchResultsAdapter();
        adapter.setOnItemClickListener(this);
        rvSearchResults.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String query = etSearchQuery.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        NaverLocalSearchService searchService = APIClient.getNaverLocalSearchRetrofit().create(NaverLocalSearchService.class);
        Call<NaverLocalSearchResponseDTO> call = searchService.searchLocal(
                query,
                5, // display: 한 번에 표시할 검색 결과 개수 (최대 5)
                1,  // start
                "random", // sort: 검색 결과 정렬 방법
                CLIENT_ID,
                CLIENT_SECRET
        );

        call.enqueue(new Callback<NaverLocalSearchResponseDTO>() {
            @Override
            public void onResponse(Call<NaverLocalSearchResponseDTO> call, Response<NaverLocalSearchResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NaverLocalSearchResponseDTO dto = response.body();
                    if (dto.getItems() != null && !dto.getItems().isEmpty()) {
                        adapter.setItems(dto.getItems());
                    } else {
                        Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        adapter.setItems(new ArrayList<>()); // 빈 리스트 전달
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("SearchActivity", "API 응답 오류: " + errorBody);
                    } catch (Exception e) {
                        Log.e("SearchActivity", "API 응답 오류를 읽는 중 예외 발생: " + e.getMessage());
                    }
                    adapter.setItems(new ArrayList<>()); // 빈 리스트 전달
                }
            }

            @Override
            public void onFailure(Call<NaverLocalSearchResponseDTO> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                Log.e("SearchActivity", "네트워크 오류: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(NaverLocalSearchResponseDTO.Item item) {
        // 선택한 주소의 정보를 MainActivity로 전달
        Intent resultIntent = new Intent();
        resultIntent.putExtra("addressNew", item.getRoadAddress());
        resultIntent.putExtra("addressOld", item.getAddress());

        double mapx = item.getMapx();
        double mapy = item.getMapy();

        double longitude = mapx / 1e7; // 경도
        double latitude = mapy / 1e7; // 위도

        Log.d("SearchActivity", "변환된 좌표: Latitude=" + latitude + ", Longitude=" + longitude);

        // 변환된 좌표를 MainActivity로 전달
        resultIntent.putExtra("latitude", latitude);
        resultIntent.putExtra("longitude", longitude);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    /**
     * 좌표 변환 결과를 전달하는 콜백 인터페이스 (현재 필요 없음)
     */
    interface CoordinateConversionCallback {
        void onConversionSuccess(double latitude, double longitude);
        void onConversionFailure(String errorMessage);
    }
}
