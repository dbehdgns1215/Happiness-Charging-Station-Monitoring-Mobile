package com.example.happy_dream_app.Util;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

public class CoordinateConverter {
    /**
     * EPSG:3857 (Web Mercator) 좌표를 EPSG:4326 (WGS84) 좌표로 변환합니다.
     *
     * @param mapx  EPSG:3857 X 좌표
     * @param mapy  EPSG:3857 Y 좌표
     * @return      변환된 {위도, 경도}
     */
    public static double[] convertEPSG3857toEPSG4326(double mapx, double mapy) {
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem srcCrs = crsFactory.createFromName("EPSG:3857");
        CoordinateReferenceSystem tgtCrs = crsFactory.createFromName("EPSG:4326");

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(srcCrs, tgtCrs);

        ProjCoordinate srcCoord = new ProjCoordinate(mapx, mapy);
        ProjCoordinate tgtCoord = new ProjCoordinate();

        transform.transform(srcCoord, tgtCoord);

        return new double[]{tgtCoord.y, tgtCoord.x}; // {위도, 경도}
    }
}

