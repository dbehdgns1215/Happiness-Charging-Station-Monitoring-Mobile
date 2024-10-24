package com.example.happy_dream_app.DTO;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ChargerDTO {
    private Integer id;
    private String name;
    private String city1;
    private String city2;
    private Integer city2Code;
    private String addressNew;
    private String addressOld;
    private String addressDetail;
    private Double latitude;
    private Double longitude;

    @SerializedName("weekdayOpen")
    private String weekdayOpen;
    @SerializedName("saturdayOpen")
    private String saturdayOpen;
    @SerializedName("holidayOpen")
    private String holidayOpen;
    @SerializedName("weekdayClose")
    private String weekdayClose;
    @SerializedName("saturdayClose")
    private String saturdayClose;
    @SerializedName("holidayClose")
    private String holidayClose;

    private Integer chargerCount;
    private Boolean chargeAirYn;
    private Boolean chargePhoneYn;
    private String callNumber;
    private LocalDate updatedDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean deletedYn;
    private LocalDateTime deletedAt;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public Integer getCity2Code() {
        return city2Code;
    }

    public void setCity2Code(Integer city2Code) {
        this.city2Code = city2Code;
    }

    public String getAddressNew() {
        return addressNew;
    }

    public void setAddressNew(String addressNew) {
        this.addressNew = addressNew;
    }

    public String getAddressOld() {
        return addressOld;
    }

    public void setAddressOld(String addressOld) {
        this.addressOld = addressOld;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getWeekdayOpen() {
        return weekdayOpen;
    }

    public void setWeekdayOpen(String weekdayOpen) {
        this.weekdayOpen = weekdayOpen;
    }

    public String getSaturdayOpen() {
        return saturdayOpen;
    }

    public void setSaturdayOpen(String saturdayOpen) {
        this.saturdayOpen = saturdayOpen;
    }

    public String getHolidayOpen() {
        return holidayOpen;
    }

    public void setHolidayOpen(String holidayOpen) {
        this.holidayOpen = holidayOpen;
    }

    public String getWeekdayClose() {
        return weekdayClose;
    }

    public void setWeekdayClose(String weekdayClose) {
        this.weekdayClose = weekdayClose;
    }

    public String getSaturdayClose() {
        return saturdayClose;
    }

    public void setSaturdayClose(String saturdayClose) {
        this.saturdayClose = saturdayClose;
    }

    public String getHolidayClose() {
        return holidayClose;
    }

    public void setHolidayClose(String holidayClose) {
        this.holidayClose = holidayClose;
    }

    public Integer getChargerCount() {
        return chargerCount;
    }

    public void setChargerCount(Integer chargerCount) {
        this.chargerCount = chargerCount;
    }

    public Boolean getChargeAirYn() {
        return chargeAirYn;
    }

    public void setChargeAirYn(Boolean chargeAirYn) {
        this.chargeAirYn = chargeAirYn;
    }

    public Boolean getChargePhoneYn() {
        return chargePhoneYn;
    }

    public void setChargePhoneYn(Boolean chargePhoneYn) {
        this.chargePhoneYn = chargePhoneYn;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getDeletedYn() {
        return deletedYn;
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
