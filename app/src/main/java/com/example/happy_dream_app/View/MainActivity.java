package com.example.happy_dream_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ChargerDTO;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.ChargerService;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ChargerService chargerService;
    private NaverMap naverMap;

    // UI 요소 선언
    private ScrollView infoScroll;
    private TextView chargerName, city1, city2, addressNew, addressOld,
            addressDetail, weekdayOpen, saturdayOpen, holidayOpen,
            weekdayClose, saturdayClose, holidayClose, chargerCount, chargeAirYn,
            chargePhoneYn, callNumber, updatedDate, createdAt, modifiedAt;

    // 버튼 선언
    private Button btnClose, btnAddReview;

    // 선택된 충전소 ID 저장 변수
    private int selectedChargerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargerService = APIClient.getRetrofit().create(ChargerService.class);

        // MapFragment 초기화
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // UI 요소 초기화
        infoScroll = findViewById(R.id.info_scroll);
        chargerName = findViewById(R.id.charger_name);
        city1 = findViewById(R.id.city1);
        city2 = findViewById(R.id.city2);
        addressNew = findViewById(R.id.address_new);
        addressOld = findViewById(R.id.address_old);
        addressDetail = findViewById(R.id.address_detail);
        weekdayOpen = findViewById(R.id.weekday_open);
        saturdayOpen = findViewById(R.id.saturday_open);
        holidayOpen = findViewById(R.id.holiday_open);
        weekdayClose = findViewById(R.id.weekday_close);
        saturdayClose = findViewById(R.id.saturday_close);
        holidayClose = findViewById(R.id.holiday_close);
        chargerCount = findViewById(R.id.charger_count);
        chargeAirYn = findViewById(R.id.charge_air_yn);
        chargePhoneYn = findViewById(R.id.charge_phone_yn);
        callNumber = findViewById(R.id.call_number);
        updatedDate = findViewById(R.id.updated_date);
        createdAt = findViewById(R.id.created_at);
        modifiedAt = findViewById(R.id.modified_at);

        // 버튼 초기화
        btnClose = findViewById(R.id.btn_close);
        btnAddReview = findViewById(R.id.btn_add_review);

        // 닫기 버튼 클릭 이벤트 처리
        btnClose.setOnClickListener(v -> infoScroll.setVisibility(View.GONE));

        // 리뷰 추가 버튼 클릭 이벤트 처리
        btnAddReview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
            intent.putExtra("chargerId", selectedChargerId);
            intent.putExtra("chargerName", chargerName.getText().toString());
            intent.putExtra("address", addressNew.getText().toString());
            intent.putExtra("addressDetail", addressDetail.getText().toString());
            startActivity(intent);
        });

    }

    @Override
    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;
        fetchChargerData();
    }

    private void fetchChargerData() {
        Call<ResponseDTO<List<ChargerDTO>>> call = chargerService.getChargerData();
        call.enqueue(new Callback<ResponseDTO<List<ChargerDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<ChargerDTO>>> call, Response<ResponseDTO<List<ChargerDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChargerDTO> chargers = response.body().getData();
                    showMarkersOnMap(chargers);
                } else {
                    Log.e("API Error", "Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<List<ChargerDTO>>> call, Throwable t) {
                Log.e("API Error", "Network Error: " + t.getMessage());
            }
        });
    }

    private void showMarkersOnMap(List<ChargerDTO> chargers) {
        for (ChargerDTO charger : chargers) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(charger.getLatitude(), charger.getLongitude()));
            marker.setCaptionText(charger.getName());

            marker.setOnClickListener(overlay -> {
                showChargerInfo(charger);
                return true;
            });

            marker.setMap(naverMap);
        }
    }

    private void showChargerInfo(ChargerDTO charger) {
        selectedChargerId = charger.getId();  // 충전소 ID 저장
        chargerName.setText("충전소 이름: " + charger.getName());
        city1.setText("광역시/도: " + charger.getCity1());
        city2.setText("시군구: " + charger.getCity2());
        addressNew.setText("도로명 주소: " + charger.getAddressNew());
        addressOld.setText("구 주소: " + charger.getAddressOld());
        addressDetail.setText("상세 주소: " + charger.getAddressDetail());
        weekdayOpen.setText("평일 운영 시간: " + charger.getWeekdayOpen());
        saturdayOpen.setText("토요일 운영 시간: " + charger.getSaturdayOpen());
        holidayOpen.setText("일요일/공휴일 운영 시간: " + charger.getHolidayOpen());
        weekdayClose.setText("평일 마감 시간: " + charger.getWeekdayClose());
        saturdayClose.setText("토요일 마감 시간: " + charger.getSaturdayClose());
        holidayClose.setText("일요일/공휴일 마감 시간: " + charger.getHolidayClose());
        chargerCount.setText("충전기 수: " + charger.getChargerCount());
        chargeAirYn.setText("에어 충전 가능: " + (charger.getChargeAirYn() ? "가능" : "불가능"));
        chargePhoneYn.setText("휴대폰 충전 가능: " + (charger.getChargePhoneYn() ? "가능" : "불가능"));
        callNumber.setText("관리 담당 전화번호: " + charger.getCallNumber());
        updatedDate.setText("충전소 정보 업데이트 날짜: " + charger.getUpdatedDate().toString());
        createdAt.setText("데이터 생성일: " + charger.getCreatedAt().toString());
        modifiedAt.setText("데이터 수정일: " + charger.getModifiedAt().toString());

        // 정보 창을 화면에 표시
        infoScroll.setVisibility(View.VISIBLE);
    }

    // 마이페이지 열기
    public void openProfilePage(android.view.View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
