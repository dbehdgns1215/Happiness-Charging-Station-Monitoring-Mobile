package com.example.happy_dream_app.DTO;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class ReviewListDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("chargerId")
    private int chargerId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("reviewContent")
    private String reviewContent;

    @SerializedName("rating")
    private float rating;

    @SerializedName("createdAt")
    private String   createdAt;

    @SerializedName("modifiedAt")
    private String  modifiedAt;

    // 기본 생성자
    public ReviewListDTO() {}

    // 모든 필드를 포함하는 생성자
    public ReviewListDTO(int id, int chargerId, int userId, String reviewContent, float rating, String  createdAt, String  modifiedAt) {
        this.id = id;
        this.chargerId = chargerId;
        this.userId = userId;
        this.reviewContent = reviewContent;
        this.rating = rating;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getChargerId() {
        return chargerId;
    }
    public void setChargerId(int chargerId){
        this.chargerId = chargerId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getReviewContent() {
        return reviewContent;
    }
    public void setReviewContent(String reviewContent){
        this.reviewContent = reviewContent;
    }

    public float getRating() {
        return rating;
    }
    public void setRating(float rating){
        this.rating = rating;
    }

    public String  getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String  createdAt){
        this.createdAt = createdAt;
    }

    public String  getModifiedAt() {
        return modifiedAt;
    }
    public void setModifiedAt(String  modifiedAt){
        this.modifiedAt = modifiedAt;
    }


    // 작성 날짜를 포맷팅하여 반환하는 메서드
    public String getFormattedDate() {
        if (createdAt != null && !createdAt.isEmpty()) {
            try {
                // 서버에서 받은 날짜 문자열을 LocalDateTime으로 파싱
                DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTime = LocalDateTime.parse(createdAt, isoFormatter);

                // 원하는 출력 형식으로 포맷팅
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return dateTime.format(outputFormatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return "날짜 형식 오류";
            }
        } else {
            return "날짜 정보 없음";
        }
    }

    public LocalDateTime getCreatedAtAsDateTime() {
        if (createdAt != null && !createdAt.isEmpty()) {
            try {
                // 날짜 형식에 맞게 포맷터 설정
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                return LocalDateTime.parse(createdAt, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
