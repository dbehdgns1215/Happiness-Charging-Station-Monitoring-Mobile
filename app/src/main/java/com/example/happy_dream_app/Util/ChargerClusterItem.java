package com.example.happy_dream_app.Util;

import com.example.happy_dream_app.DTO.ChargerDetailDTO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.clustering.ClusteringKey;
import java.util.List;

public class ChargerClusterItem implements ClusteringKey {
    private final LatLng position;
    private final List<ChargerDetailDTO> chargersAtLocation;

    public ChargerClusterItem(double lat, double lng, List<ChargerDetailDTO> chargersAtLocation) {
        position = new LatLng(lat, lng);
        this.chargersAtLocation = chargersAtLocation;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public List<ChargerDetailDTO> getChargersAtLocation() {
        return chargersAtLocation;
    }
}
