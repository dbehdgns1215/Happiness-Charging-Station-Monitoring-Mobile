package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.DTO.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    @POST("/api/v1/login")
    Call<ResponseDTO> login(@Body UserDTO userDTO);
}
