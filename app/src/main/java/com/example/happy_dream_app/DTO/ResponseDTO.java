package com.example.happy_dream_app.DTO;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.List;

public class ResponseDTO<T> {
    @SerializedName("api_version")
    private String apiVersion;
    private String status;
    @SerializedName("response_code")
    private Integer responseCode;

    private LocalDateTime time = LocalDateTime.now();
    private String message;
    private Integer count;
    private T data;

    // Getters and Setters

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
