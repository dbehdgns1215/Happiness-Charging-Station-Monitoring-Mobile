package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.DTO.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SignupService {
    @POST("/api/v1/users")
    Call<ResponseDTO> signup(
            @retrofit2.http.Query("username") String username,
            @Body UserDTO userDTO
    );
}