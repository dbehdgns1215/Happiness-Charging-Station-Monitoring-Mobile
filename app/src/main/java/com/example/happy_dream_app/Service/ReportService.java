package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ReportRequest;
import com.example.happy_dream_app.DTO.ResponseDTO;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportService {

    @POST("/api/v1/reports")
    Call<ResponseDTO> submitReport(@Body ReportRequest reportRequest);
}
