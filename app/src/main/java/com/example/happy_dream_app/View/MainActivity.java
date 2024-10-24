package com.example.happy_dream_app.View;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happy_dream_app.APIClient;
import com.example.happy_dream_app.DTO.ChargerDTO;
import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.R;
import com.example.happy_dream_app.Service.ChargerService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ChargerService chargerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargerService = APIClient.getRetrofit().create(ChargerService.class);
        fetchChargerData();
    }

    private void fetchChargerData() {
        Call<ResponseDTO<List<ChargerDTO>>> call = chargerService.getChargerData();
        call.enqueue(new Callback<ResponseDTO<List<ChargerDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<ChargerDTO>>> call, Response<ResponseDTO<List<ChargerDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChargerDTO> chargers = response.body().getData();

                    // Gson을 사용해 객체를 JSON 문자열로 변환
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonResult = gson.toJson(chargers);

                    // JSON 결과를 로그로 출력
                    Log.d("API Response", "Chargers JSON: " + jsonResult);
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
}
