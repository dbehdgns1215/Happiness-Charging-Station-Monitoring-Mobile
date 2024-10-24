package com.example.happy_dream_app.DTO;

import java.time.LocalDateTime;
import java.util.Date;

public class ReviewDTO {
    private int chargerId;
    private int userId;
    private String reviewContent;
    private byte rating;
    private LocalDateTime createdAt;

    // 생성자
    public ReviewDTO(int chargerId, int userId, String reviewContent, byte rating) {
        this.chargerId = chargerId;
        this.userId = userId;
        this.reviewContent = reviewContent;
        this.rating = rating;
        this.createdAt = LocalDateTime.now(); // 생성 시점으로 날짜 지정
    }

    // Getters and Setters
    public int getChargerId() {
        return chargerId;
    }

    public void setChargerId(int chargerId) {
        this.chargerId = chargerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
