package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ReviewDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReviewService {
    @POST("/api/v1/reviews")
    Call<Void> addReview(@Body ReviewDTO reviewDTO
    );
}
