package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.NaverCoordinateConvertResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverCoordinateConvertService {
    @GET("map-reversegeocode/v2/gc")
    Call<NaverCoordinateConvertResponseDTO> reverseGeocode(
            @Header("X-NCP-APIGW-API-KEY-ID") String clientId,
            @Header("X-NCP-APIGW-API-KEY") String clientSecret,
            @Query("request") String request, // 필수 파라미터
            @Query("coords") String coords,
            @Query("sourcecrs") String sourcecrs,
            @Query("output") String output,
            @Query("orders") String orders // 필수 또는 선택 파라미터
    );
}
