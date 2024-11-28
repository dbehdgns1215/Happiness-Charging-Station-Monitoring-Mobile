package com.example.happy_dream_app.DTO;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ChargerDetailDTO {
    // 츙전기 기본 데이터(Charger)
    private Integer chargerId;

    //private ChargerStateEntity chargerState;
    private String name;
    private String city1;
    private String city2;
    private Integer city2Code;
    private String addressNew;
    private String addressOld;
    private String addressDetail;
    private Double latitude;
    private Double longitude;

    // 츙전기 운영 관련 데이터(Charger)
    private String weekdayOpen;
    private String saturdayOpen;
    private String holidayOpen;
    private String weekdayClose;
    private String saturdayClose;
    private String holidayClose;


    private Integer chargerCount;
    private Boolean chargeAirYn;
    private Boolean chargePhoneYn;
    private String callNumber;

    // 충전 상태 기본 데이터 (ChargerState)
    private Integer chargerStateId;
    private Boolean usingYn;
    private Boolean brokenYn;

    // 관리 목적 데이터 (Charger)
    private LocalDate updatedDate;
    private LocalDateTime chargerCreatedAt;
    private LocalDateTime chargerModifiedAt;
    private Boolean deletedYn;
    private LocalDateTime deletedAt;

    // 관리 목적 데이터 (ChargerState)
    private LocalDateTime usingAt;
    private LocalDateTime brokenAt;
    private LocalDateTime chargerStateCreatedAt;
    private LocalDateTime chargerStateModifiedAt;

    // 충전기 리뷰 관련 데이터 (Review)
    private Double rating;


    public Integer getChargerId() {
        return chargerId;
    }

    public void setChargerId(Integer chargerId) {
        this.chargerId = chargerId;
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

    public Integer getChargerStateId() {
        return chargerStateId;
    }

    public void setChargerStateId(Integer chargerStateId) {
        this.chargerStateId = chargerStateId;
    }

    public Boolean getUsingYn() {
        return usingYn;
    }

    public void setUsingYn(Boolean usingYn) {
        this.usingYn = usingYn;
    }

    public Boolean getBrokenYn() {
        return brokenYn;
    }

    public void setBrokenYn(Boolean brokenYn) {
        this.brokenYn = brokenYn;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getChargerCreatedAt() {
        return chargerCreatedAt;
    }

    public void setChargerCreatedAt(LocalDateTime chargerCreatedAt) {
        this.chargerCreatedAt = chargerCreatedAt;
    }

    public LocalDateTime getChargerModifiedAt() {
        return chargerModifiedAt;
    }

    public void setChargerModifiedAt(LocalDateTime chargerModifiedAt) {
        this.chargerModifiedAt = chargerModifiedAt;
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

    public LocalDateTime getUsingAt() {
        return usingAt;
    }

    public void setUsingAt(LocalDateTime usingAt) {
        this.usingAt = usingAt;
    }

    public LocalDateTime getBrokenAt() {
        return brokenAt;
    }

    public void setBrokenAt(LocalDateTime brokenAt) {
        this.brokenAt = brokenAt;
    }

    public LocalDateTime getChargerStateCreatedAt() {
        return chargerStateCreatedAt;
    }

    public void setChargerStateCreatedAt(LocalDateTime chargerStateCreatedAt) {
        this.chargerStateCreatedAt = chargerStateCreatedAt;
    }

    public LocalDateTime getChargerStateModifiedAt() {
        return chargerStateModifiedAt;
    }

    public void setChargerStateModifiedAt(LocalDateTime chargerStateModifiedAt) {
        this.chargerStateModifiedAt = chargerStateModifiedAt;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
