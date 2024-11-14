package com.example.happy_dream_app.View;

import static com.naver.maps.map.util.MarkerIcons.RED;
import static com.naver.maps.map.util.MarkerIcons.YELLOW;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ChargerDetailDTO;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.ChargerService;
import com.example.happy_dream_app.Util.ChargerClusterItem;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.clustering.LeafMarkerInfo;
import com.naver.maps.map.clustering.LeafMarkerUpdater;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.clustering.Clusterer;
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final int SEARCH_REQUEST_CODE = 2000;
    private FusedLocationSource locationSource;
    private ChargerService chargerService;
    private NaverMap naverMap;
    private Clusterer<ChargerClusterItem> clusterer;
    private List<ChargerDetailDTO> chargerDataList = new ArrayList<>();

    // UI 요소 선언
    private ScrollView infoScroll;
    private TextView chargerName, city1, city2, addressNew, addressOld,
            addressDetail, weekdayOpen, saturdayOpen, holidayOpen,
            weekdayClose, saturdayClose, holidayClose, chargerCount, chargeAirYn,
            chargePhoneYn, callNumber, updatedDate, createdAt, modifiedAt;

    // 버튼 선언
    private Button btnClose, btnAddReview;
    private ImageButton btnSearch, btnRefresh;

    // 선택된 충전소 ID 저장 변수
    private int selectedChargerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargerService = APIClient.getRetrofit().create(ChargerService.class);

        // 위치 소스 초기화
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // MapFragment 초기화
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        initializeUIElements();

        // 검색 버튼 초기화 및 클릭 이벤트 설정
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(intent, SEARCH_REQUEST_CODE);
        });

        // 새로고침 버튼 설정
        btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(v -> fetchChargerData());
    }

    @Override
    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;
        fetchChargerData();

        // Clusterer 초기화
        clusterer = new Clusterer.Builder<ChargerClusterItem>()
                .minZoom(3)
                .maxZoom(16)
                .screenDistance(40)
                .animate(false)
                .leafMarkerUpdater(new LeafMarkerUpdater() {
                    @Override
                    public void updateLeafMarker(LeafMarkerInfo info, Marker marker) {
                        marker.setOnClickListener(overlay -> {
                            ChargerClusterItem item = (ChargerClusterItem) info.getKey();
                            showChargerInfo(item.getChargerDetail());
                            return true;
                        });
                    }
                })
                .build();
        clusterer.setMap(naverMap);

        // 줌 레벨 변경 감지 및 최적화
        naverMap.addOnCameraChangeListener((reason, animated) -> {
            double zoom = naverMap.getCameraPosition().zoom;

            if (zoom >= 16) {
                // 클러스터링 해제
                showIndividualMarkers();
            } else {
                // 클러스터링 활성화
                hideIndividualMarkers();
                if (clusterer != null && clusterer.isEmpty()) { // 한 번만 생성되게끔, 이후에는 재활용
                    addClusterItems(chargerDataList);
                }
            }
        });

        // 위치 소스 설정
        naverMap.setLocationSource(locationSource);

        // 위치 버튼 활성화
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        // 위치 오버레이 설정
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        // 위치 추적 모드 설정
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // 개별 마커 리스트
    private List<Marker> individualMarkers = new ArrayList<>();

    // 개별 마커 보이기
    private void showIndividualMarkers() {
        OverlayImage usingNow = YELLOW;
        OverlayImage brokenNow = RED;
        if (individualMarkers.isEmpty()) {
            double offset = 0.00000001;    // 오프셋 값

            for (int i = 0; i < chargerDataList.size(); i++) {
                ChargerDetailDTO charger = chargerDataList.get(i);

                double latitude = charger.getLatitude() + (i * offset);
                double longitude = charger.getLongitude() + (i * offset);

                charger.getUsingYn();
                charger.getBrokenYn();

                Marker marker = new Marker();
                marker.setPosition(new LatLng(latitude, longitude));

                // 상태에 따라 아이콘 설정
                if (charger.getUsingYn()) {
                    marker.setIcon(usingNow);  // 사용 중일 때 YELLOW 아이콘
                } else if (charger.getBrokenYn()) {
                    marker.setIcon(brokenNow); // 고장일 때 RED 아이콘
                }

                marker.setMap(naverMap);
                marker.setOnClickListener(overlay -> {
                    showChargerInfo(charger);
                    return true;
                });
                individualMarkers.add(marker);
            }
        } else {
            for (Marker marker : individualMarkers) {
                marker.setMap(naverMap); // 기존 마커를 재활용하여 표시
            }
        }
        clusterer.setMap(null); // 클러스터링 해제
    }

    // 개별 마커 숨기기
    private void hideIndividualMarkers() {
        for (Marker marker : individualMarkers) {
            marker.setMap(null); // 마커 숨기기
        }
        clusterer.setMap(naverMap); // 클러스터링 활성화
    }

    private void fetchChargerData() {
        Call<ResponseDTO<List<ChargerDetailDTO>>> call = chargerService.getChargerData();
        call.enqueue(new Callback<ResponseDTO<List<ChargerDetailDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<ChargerDetailDTO>>> call,
                                   Response<ResponseDTO<List<ChargerDetailDTO>>> response) {
                Log.i("API Response", "Received response");
                if (response.isSuccessful() && response.body() != null) {
                    chargerDataList = response.body().getData();
                    addClusterItems(chargerDataList);
                } else {
                    Log.e("API Error", "Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<List<ChargerDetailDTO>>> call, Throwable t) {
                Log.e("API Error", "Network Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void addClusterItems(List<ChargerDetailDTO> chargers) {
        Map<ChargerClusterItem, Object> clusterItems = new HashMap<>();
        double offset = 0.00000001;  // 오프셋 값
        for (int i = 0; i < chargers.size(); i++) {
            ChargerDetailDTO charger = chargers.get(i);
            double latitude = charger.getLatitude() + (i * offset);
            double longitude = charger.getLongitude() + (i * offset);
            ChargerClusterItem clusterItem = new ChargerClusterItem(latitude, longitude, charger);
            clusterItems.put(clusterItem, null);
        }
        runOnUiThread(() -> {
            try {
                clusterer.addAll(clusterItems);
            } catch (Exception e) {
                Log.e("Cluster Add Error", e.getMessage());
            }
        });
    }

    private void initializeUIElements() {
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

    private void showChargerInfo(ChargerDetailDTO charger) {
        selectedChargerId = charger.getChargerId();  // 충전소 ID 저장
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
        createdAt.setText("데이터 생성일: " + charger.getChargerCreatedAt().toString());
        modifiedAt.setText("데이터 수정일: " + charger.getChargerModifiedAt().toString());

        // 정보 창을 화면에 표시
        infoScroll.setVisibility(View.VISIBLE);
    }

    // 마이페이지 열기
    public void openProfilePage(android.view.View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String addressNew = data.getStringExtra("addressNew");
            String addressOld = data.getStringExtra("addressOld");
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);

            Log.d("MainActivity", "Received from SearchActivity - Latitude: " + latitude + ", Longitude: " + longitude);

            if (latitude != 0.0 && longitude != 0.0) {
                moveMapToLocation(latitude, longitude);
                Toast.makeText(this, "검색 주소로 이동했습니다: " + addressNew, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void moveMapToLocation(double latitude, double longitude) {
        if (naverMap == null) {
            Log.e("MainActivity", "NaverMap is null");
            return;
        }

        // 위치 추적 모드를 비활성화하여 카메라가 자동으로 돌아오지 않도록 함
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);

        LatLng target = new LatLng(latitude, longitude);
        Log.d("MainActivity", "Moving camera to: Latitude=" + latitude + ", Longitude=" + longitude);

        // scrollAndZoomTo를 사용하여 카메라 위치와 줌을 설정
        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(target, 17)
                .animate(CameraAnimation.Easing);

        // 카메라 이동 실행
        naverMap.moveCamera(cameraUpdate);
    }

}