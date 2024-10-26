package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ReviewDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {
    @POST("/reviews")
    Call<Void> addReview(
            @retrofit2.http.Query("charger_id") int chargerId,
            @Body ReviewDTO reviewDTO
    );
}
