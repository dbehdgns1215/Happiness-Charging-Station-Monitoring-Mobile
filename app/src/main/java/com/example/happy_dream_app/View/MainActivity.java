package com.example.happy_dream_app.View;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ChargerService 초기화
        chargerService = APIClient.getRetrofit().create(ChargerService.class);

        // MapFragment를 가져와서 콜백을 등록
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;
        fetchChargerData();
    }

    // 서버에서 데이터를 가져오는 메서드
    private void fetchChargerData() {
        Call<ResponseDTO<List<ChargerDTO>>> call = chargerService.getChargerData();
        call.enqueue(new Callback<ResponseDTO<List<ChargerDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<ChargerDTO>>> call, Response<ResponseDTO<List<ChargerDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChargerDTO> chargers = response.body().getData();
                    showMarkersOnMap(chargers);  // 데이터를 기반으로 마커 추가
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

    // 데이터를 바탕으로 지도에 마커를 추가하는 메서드
    private void showMarkersOnMap(List<ChargerDTO> chargers) {
        for (ChargerDTO charger : chargers) {
            Marker marker = new Marker();
            marker.setPosition(new LatLng(charger.getLatitude(), charger.getLongitude()));
            marker.setCaptionText(charger.getName());  // 마커에 캡션 설정 (충전소 이름)
            marker.setMap(naverMap);  // 마커를 지도에 추가
        }
    }
}
