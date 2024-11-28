package com.example.happy_dream_app.View;

import static com.naver.maps.map.util.MarkerIcons.RED;
import static com.naver.maps.map.util.MarkerIcons.YELLOW;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ChargerDetailDTO;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.ChargerService;
import com.example.happy_dream_app.Util.ChargerClusterItem;
import com.example.happy_dream_app.Util.ChargerListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private Map<ChargerClusterItem, Object> clusterItems = new HashMap<>();
    private List<ChargerDetailDTO> chargerDataList = new ArrayList<>();

    // UI 요소 선언
    private ScrollView infoScroll;
    private TextView chargerName, addressNew, addressOld, addressDetail,
            chargerCount, chargeAirYn, weekdayHours, saturdayHours, holidayHours,
            chargePhoneYn, callNumber, updatedDate, createdAt, modifiedAt, ratingTextView;

    private RatingBar chargerRating; // 별점 뷰 추가

    // 버튼 선언
    private Button btnClose, btnAddReview, btnReport;
    private ImageButton btnSearch, btnRefresh;

    // 선택된 충전소 ID 저장 변수
    private int selectedChargerId;

    // 마커 상태 추적 변수
    private boolean isShowingIndividualMarkers = false;

    // 개별 마커 리스트
    private List<Marker> individualMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargerService = APIClient.getRetrofit().create(ChargerService.class);

        // 위치 소스 초기화
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // MapFragment 초기화
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

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

        // 업데이트 정보 버튼
        ImageButton btnInfo = findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(v -> showUpdateInfo());
    }

    @Override
    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;

        // Clusterer 초기화
        clusterer = new Clusterer.Builder<ChargerClusterItem>()
                .minZoom(3)
                .maxZoom(13)
                .screenDistance(80)
                .animate(false)
                .leafMarkerUpdater(new LeafMarkerUpdater() {
                    @Override
                    public void updateLeafMarker(LeafMarkerInfo info, Marker marker) {
                        ChargerClusterItem item = (ChargerClusterItem) info.getKey();
                        List<ChargerDetailDTO> chargersAtLocation = item.getChargersAtLocation();

                        // 마커 아이콘 설정
                        if (chargersAtLocation.size() > 1) {
                            boolean hasAvailable = false;
                            boolean hasUsing = false;
                            boolean allBroken = true; // 모든 충전기가 고장났는지 여부

                            for (ChargerDetailDTO charger : chargersAtLocation) {
                                if (charger.getBrokenYn() != null && charger.getBrokenYn()) {
                                    // 고장난 충전기이므로 아무것도 하지 않음
                                    continue;
                                } else {
                                    allBroken = false; // 고장나지 않은 충전기가 있으므로 false로 설정

                                    if (charger.getUsingYn() != null && charger.getUsingYn()) {
                                        hasUsing = true; // 사용 중인 충전기가 있음
                                    } else {
                                        hasAvailable = true; // 사용 가능한 충전기가 있음
                                        break; // 사용 가능한 충전기가 있으므로 더 이상 검사할 필요 없음
                                    }
                                }
                            }

                            if (hasAvailable) {
                                marker.setIcon(OverlayImage.fromResource(R.drawable.marker_list_green)); // 초록색 아이콘
                            } else if (hasUsing) {
                                marker.setIcon(OverlayImage.fromResource(R.drawable.marker_list_yellow)); // 노란색 아이콘
                            } else if (allBroken) {
                                marker.setIcon(OverlayImage.fromResource(R.drawable.marker_list_red)); // 빨간색 아이콘
                            } else {
                                marker.setIcon(Marker.DEFAULT_ICON); // 기본적으로 초록색 아이콘
                            }
                        } else {
                            // 하나의 충전소만 있는 경우 상태에 따라 아이콘 설정
                            ChargerDetailDTO charger = chargersAtLocation.get(0);
                            if (charger.getBrokenYn() != null && charger.getBrokenYn()) {
                                marker.setIcon(OverlayImage.fromResource(R.drawable.marker_red));
                            } else if (charger.getUsingYn() != null && charger.getUsingYn()) {
                                marker.setIcon(OverlayImage.fromResource(R.drawable.marker_yellow));
                            } else {
                                marker.setIcon(OverlayImage.fromResource(R.drawable.marker_green));
                            }
                            marker.setCaptionText(null);
                        }

                        // 마커 클릭 이벤트 설정
                        marker.setOnClickListener(overlay -> {
                            showChargersAtLocation(chargersAtLocation);
                            return true;
                        });
                    }
                })
                .build();
        clusterer.setMap(naverMap);

        // 줌 레벨 변경 감지 및 마커 가시성 조정
        naverMap.addOnCameraChangeListener((reason, animated) -> {
            double zoom = naverMap.getCameraPosition().zoom;
            boolean shouldShowIndividualMarkers = zoom >= 14;

            if (isShowingIndividualMarkers != shouldShowIndividualMarkers) {
                if (shouldShowIndividualMarkers) {
                    showIndividualMarkers();
                } else {
                    hideIndividualMarkers();
                    showClusterItems();
                }
                isShowingIndividualMarkers = shouldShowIndividualMarkers;
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

        fetchChargerData();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("정말 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예", (dialog, id) -> {
                    MainActivity.super.onBackPressed(); // 앱 종료
                })
                .setNegativeButton("아니오", null)
                .show();
    }


    private void fetchChargerData() {
        // API 호출
        Call<ResponseDTO<List<ChargerDetailDTO>>> call = chargerService.getChargerData();
        call.enqueue(new Callback<ResponseDTO<List<ChargerDetailDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<ChargerDetailDTO>>> call,
                                   Response<ResponseDTO<List<ChargerDetailDTO>>> response) {
                Log.i("API Response", "Received response");
                if (response.isSuccessful() && response.body() != null) {
                    chargerDataList = response.body().getData();

                    runOnUiThread(() -> {
                        // 마커와 클러스터 아이템 초기화
                        initializeMarkers();

                        // 현재 줌 레벨에 따라 마커 또는 클러스터 표시
                        double zoom = naverMap.getCameraPosition().zoom;
                        if (zoom >= 14) {
                            showIndividualMarkers();
                        } else {
                            hideIndividualMarkers();
                            showClusterItems();
                        }
                    });
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

    private void initializeMarkers() {
        // 기존 마커 및 클러스터 아이템 제거
        for (Marker marker : individualMarkers) {
            marker.setMap(null);
        }
        individualMarkers.clear();

        // 클러스터러의 아이템을 명시적으로 제거
        clusterer.clear();
        clusterItems.clear();

        // 위치별로 충전소 그룹핑
        Map<String, List<ChargerDetailDTO>> locationMap = new HashMap<>();

        double gridSize = 0.0001;

        for (ChargerDetailDTO charger : chargerDataList) {
            // 그리드 키 생성
            String key = getGridKey(charger.getLatitude(), charger.getLongitude(), gridSize);
            locationMap.computeIfAbsent(key, k -> new ArrayList<>()).add(charger);
        }

        // 그룹핑된 위치별로 마커 생성
        for (Map.Entry<String, List<ChargerDetailDTO>> entry : locationMap.entrySet()) {
            String[] indices = entry.getKey().split(",");
            int latIndex = Integer.parseInt(indices[0]);
            int lngIndex = Integer.parseInt(indices[1]);
            double latitude = (latIndex + 0.5) * gridSize;
            double longitude = (lngIndex + 0.5) * gridSize;
            List<ChargerDetailDTO> chargersAtSameLocation = entry.getValue();

            // 마커 생성
            Marker marker = new Marker();
            marker.setPosition(new LatLng(latitude, longitude));

            // 마커 아이콘 설정
            if (chargersAtSameLocation.size() > 1) {
                boolean hasAvailable = false;
                boolean hasUsing = false;
                boolean allBroken = true; // 모든 충전기가 고장났는지 여부

                for (ChargerDetailDTO charger : chargersAtSameLocation) {
                    if (charger.getBrokenYn() != null && charger.getBrokenYn()) {
                        // 고장난 충전기이므로 아무것도 하지 않음
                        continue;
                    } else {
                        allBroken = false; // 고장나지 않은 충전기가 있으므로 false로 설정

                        if (charger.getUsingYn() != null && charger.getUsingYn()) {
                            hasUsing = true; // 사용 중인 충전기가 있음
                        } else {
                            hasAvailable = true; // 사용 가능한 충전기가 있음
                            break; // 사용 가능한 충전기가 있으므로 더 이상 검사할 필요 없음
                        }
                    }
                }

                if (hasAvailable) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_list_green)); // 초록색 아이콘
                } else if (hasUsing) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_list_yellow)); // 노란색 아이콘
                } else if (allBroken) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_list_red)); // 빨간색 아이콘
                } else {
                    marker.setIcon(Marker.DEFAULT_ICON); // 기본적으로 초록색 아이콘
                }
            } else {
                // 하나의 충전소만 있는 경우 상태에 따라 아이콘 설정
                ChargerDetailDTO charger = chargersAtSameLocation.get(0);
                if (charger.getBrokenYn() != null && charger.getBrokenYn()) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_red));
                } else if (charger.getUsingYn() != null && charger.getUsingYn()) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_yellow));
                } else {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_green));
                }
                marker.setCaptionText(null);
            }


            // 마커 클릭 이벤트 설정
            marker.setOnClickListener(overlay -> {
                if (chargersAtSameLocation.size() == 1) {
                    showChargerInfo(chargersAtSameLocation.get(0));
                } else {
                    showChargersAtLocation(chargersAtSameLocation);
                }
                return true;
            });


            marker.setMap(null); // 초기에는 지도에 추가하지 않음
            individualMarkers.add(marker);

            // 클러스터 아이템 생성
            ChargerClusterItem clusterItem = new ChargerClusterItem(latitude, longitude, chargersAtSameLocation);
            clusterItems.put(clusterItem, null);
        }

        // 클러스터 아이템 추가
        clusterer.addAll(clusterItems);
    }

    // 그리드 키를 생성하는 메서드
    private String getGridKey(double latitude, double longitude, double gridSize) {
        int latIndex = (int) Math.floor(latitude / gridSize);
        int lngIndex = (int) Math.floor(longitude / gridSize);
        return latIndex + "," + lngIndex;
    }


    private void showIndividualMarkers() {
        // 클러스터를 숨기기
        clusterer.setMap(null);

        // 개별 마커 표시
        for (Marker marker : individualMarkers) {
            marker.setMap(naverMap);
        }
    }

    private void hideIndividualMarkers() {
        // 개별 마커 숨기기
        for (Marker marker : individualMarkers) {
            marker.setMap(null);
        }

        // 클러스터 표시
        clusterer.setMap(naverMap);
    }


    private void showClusterItems() {
        // 클러스터 표시
        clusterer.setMap(naverMap);
    }

    private void showChargersAtLocation(List<ChargerDetailDTO> chargersAtLocation) {
        // BottomSheetDialog를 사용하여 충전소 목록 표시
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_charger_list, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        RecyclerView recyclerView = sheetView.findViewById(R.id.recycler_view_chargers);
        ChargerListAdapter adapter = new ChargerListAdapter(chargersAtLocation, charger -> {
            // 아이템 클릭 시 상세 정보 표시
            showChargerInfo(charger);
            dialog.dismiss();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        dialog.show();
    }

    private void initializeUIElements() {
        // UI 요소 초기화
        infoScroll = findViewById(R.id.info_scroll);
        chargerName = findViewById(R.id.charger_name);
        chargerRating = findViewById(R.id.charger_rating);
        ratingTextView = findViewById(R.id.rating_text);
        addressNew = findViewById(R.id.address_new);
        addressOld = findViewById(R.id.address_old);
        addressDetail = findViewById(R.id.address_detail);
        weekdayHours = findViewById(R.id.weekday_hours);
        saturdayHours = findViewById(R.id.saturday_hours);
        holidayHours = findViewById(R.id.holiday_hours);
        chargerCount = findViewById(R.id.charger_count);
        chargeAirYn = findViewById(R.id.charge_air_yn);
        chargePhoneYn = findViewById(R.id.charge_phone_yn);
        callNumber = findViewById(R.id.call_number);

        // 업데이트 정보 저장용 변수 초기화
        updatedDate = new TextView(this);
        createdAt = new TextView(this);
        modifiedAt = new TextView(this);

        // 버튼 초기화
        btnClose = findViewById(R.id.btn_close);
        btnAddReview = findViewById(R.id.btn_add_review);
        btnReport = findViewById(R.id.btn_report_issue);

        // 닫기 버튼 클릭 이벤트 처리
        btnClose.setOnClickListener(v -> infoScroll.setVisibility(View.GONE));

        // 리뷰 추가 버튼 클릭 이벤트 처리
        btnAddReview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
            intent.putExtra("chargerId", selectedChargerId);
            intent.putExtra("chargerName", chargerName.getText().toString());
            intent.putExtra("address", addressNew.getText().toString() + " " + addressDetail.getText().toString());
            startActivity(intent);
        });

        // 고장 신고 버튼 클릭 이벤트 처리
        btnReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra("chargerId", selectedChargerId);
            intent.putExtra("chargerName", chargerName.getText().toString());
            intent.putExtra("address", addressNew.getText().toString() + " " + addressDetail.getText().toString());
            startActivity(intent);
        });
    }

    private void showChargerInfo(ChargerDetailDTO charger) {
        selectedChargerId = charger.getChargerId();  // 충전소 ID 저장
        chargerName.setText(charger.getName());

        // 별점 설정
        if (charger.getRating() != null) {
            double ratingValue = charger.getRating().doubleValue();
            chargerRating.setRating((float) ratingValue);

            // 평균 별점을 텍스트로 표시
            String ratingText = String.format("%.1f/5", ratingValue);
            ratingTextView.setText(ratingText);
        } else {
            chargerRating.setRating(0); // 별점 정보가 없을 경우 0으로 설정
            ratingTextView.setText("0.0/5.0");
        }

        // 주소 정보 설정
        addressNew.setText(charger.getAddressNew());
        addressOld.setText(charger.getAddressOld());
        addressDetail.setText(charger.getAddressDetail());

        // 운영 시간 표시 형식 수정
        weekdayHours.setText("" + charger.getWeekdayOpen() + " ~ " + charger.getWeekdayClose());
        saturdayHours.setText("" + charger.getSaturdayOpen() + " ~ " + charger.getSaturdayClose());
        holidayHours.setText("" + charger.getHolidayOpen() + " ~ " + charger.getHolidayClose());

        // 기타 정보 설정
        chargerCount.setText("" + charger.getChargerCount());
        chargeAirYn.setText("" + (charger.getChargeAirYn() ? "가능" : "불가능"));
        chargePhoneYn.setText("" + (charger.getChargePhoneYn() ? "가능" : "불가능"));
        callNumber.setText("" + charger.getCallNumber());

        // 업데이트 정보 저장
        updatedDate.setText(charger.getUpdatedDate().toString());
        createdAt.setText(charger.getChargerCreatedAt().toString());
        modifiedAt.setText(charger.getChargerModifiedAt().toString());

        // 정보 창을 화면에 표시
        infoScroll.setVisibility(View.VISIBLE);
    }

    private void showUpdateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("업데이트 정보");

        String message = "충전소 정보 업데이트 날짜: " + updatedDate.getText().toString() + "\n"
                + "데이터 생성일: " + createdAt.getText().toString() + "\n"
                + "데이터 수정일: " + modifiedAt.getText().toString();

        builder.setMessage(message);
        builder.setPositiveButton("닫기", null);
        builder.show();
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
