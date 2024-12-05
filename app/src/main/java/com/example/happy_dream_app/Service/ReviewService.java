package com.example.happy_dream_app.Service;

import com.example.happy_dream_app.DTO.ResponseDTO;
import com.example.happy_dream_app.DTO.ReviewDTO;
import com.example.happy_dream_app.DTO.ReviewListDTO;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewService {
    @POST("/api/v1/reviews")
    Call<Void> addReview(@Body ReviewDTO reviewDTO);

    @GET("/api/v1/reviews/charger/{id}")
    Call<ResponseDTO<List<ReviewListDTO>>> getReviewsByChargerId(@Path("id") int chargerId);
}
