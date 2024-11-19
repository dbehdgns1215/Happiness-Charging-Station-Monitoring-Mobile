package com.example.happy_dream_app.Util;

import com.example.happy_dream_app.DTO.ChargerDetailDTO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.clustering.ClusteringKey;

public class ChargerClusterItem implements ClusteringKey {
    private final LatLng position;
    private final ChargerDetailDTO chargerDetail;

    public ChargerClusterItem(double lat, double lng, ChargerDetailDTO chargerDetail) {
        position = new LatLng(lat, lng);
        this.chargerDetail = chargerDetail;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public ChargerDetailDTO getChargerDetail() {
        return chargerDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargerClusterItem that = (ChargerClusterItem) o;

        return chargerDetail.getChargerId() == that.chargerDetail.getChargerId();
    }

    @Override
    public int hashCode() {
        return chargerDetail.getChargerId();
    }

}
