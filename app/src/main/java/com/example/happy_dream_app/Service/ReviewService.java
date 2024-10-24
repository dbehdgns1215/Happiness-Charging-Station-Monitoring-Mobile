package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ReviewDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {
    @POST("/reviews/{chargerId}/{userId}")
    Call<Void> addReview(
            @Path("chargerId") int chargerId,
            @Path("userId") int userId,
            @Body ReviewDTO reviewDTO
    );
}
