package com.example.happy_dream_app.Util;

import com.example.happy_dream_app.DTO.ChargerDetailDTO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.clustering.ClusteringKey;

public class ChargerClusterItem implements ClusteringKey {
    private final double latitude;
    private final double longitude;
    private final ChargerDetailDTO chargerDetail; // ChargerDetailDTO 정보를 저장하는 필드 추가

    public ChargerClusterItem(double latitude, double longitude, ChargerDetailDTO chargerDetail) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.chargerDetail = chargerDetail;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ChargerDetailDTO getChargerDetail() {
        return chargerDetail;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }
}

