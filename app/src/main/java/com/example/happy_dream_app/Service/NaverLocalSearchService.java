package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.NaverLocalSearchResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverLocalSearchService {
    @GET("local.json")
    Call<NaverLocalSearchResponseDTO> searchLocal(
            @Query("query") String query,
            @Query("display") int display,
            @Query("start") int start,
            @Query("sort") String sort,
            @Header("X-Naver-Client-Id") String clientId,
            @Header("X-Naver-Client-Secret") String clientSecret
    );
}
