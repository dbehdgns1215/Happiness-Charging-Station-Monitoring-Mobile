package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ChargerDTO;
import com.example.happy_dream_app.DTO.ResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChargerService {
    @GET("/api/v1/chargers")
    Call<ResponseDTO<List<ChargerDTO>>> getChargerData();
}
