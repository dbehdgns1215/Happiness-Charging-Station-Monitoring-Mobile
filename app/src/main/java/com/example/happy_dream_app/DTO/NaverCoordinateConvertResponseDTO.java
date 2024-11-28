package com.example.happy_dream_app.DTO;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NaverCoordinateConvertResponseDTO {

    @SerializedName("status")
    private Status status;

    @SerializedName("results")
    private List<Result> results;

    // Getters and Setters

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Status {
        @SerializedName("code")
        private int code;

        @SerializedName("name")
        private String name;

        @SerializedName("message")
        private String message;

        // Getters and Setters

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Result {
        @SerializedName("name")
        private String name;

        @SerializedName("code")
        private Code code;

        @SerializedName("region")
        private Region region;

        // Getters and Setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Code getCode() {
            return code;
        }

        public void setCode(Code code) {
            this.code = code;
        }

        public Region getRegion() {
            return region;
        }

        public void setRegion(Region region) {
            this.region = region;
        }
    }

    public static class Code {
        @SerializedName("id")
        private String id;

        @SerializedName("type")
        private String type;

        @SerializedName("mappingId")
        private String mappingId;

        // Getters and Setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMappingId() {
            return mappingId;
        }

        public void setMappingId(String mappingId) {
            this.mappingId = mappingId;
        }
    }

    public static class Region {
        @SerializedName("area0")
        private Area area0;

        @SerializedName("area1")
        private Area area1;

        @SerializedName("area2")
        private Area area2;

        @SerializedName("area3")
        private Area area3;

        @SerializedName("area4")
        private Area area4;

        // Getters and Setters

        public Area getArea0() {
            return area0;
        }

        public void setArea0(Area area0) {
            this.area0 = area0;
        }

        public Area getArea1() {
            return area1;
        }

        public void setArea1(Area area1) {
            this.area1 = area1;
        }

        public Area getArea2() {
            return area2;
        }

        public void setArea2(Area area2) {
            this.area2 = area2;
        }

        public Area getArea3() {
            return area3;
        }

        public void setArea3(Area area3) {
            this.area3 = area3;
        }

        public Area getArea4() {
            return area4;
        }

        public void setArea4(Area area4) {
            this.area4 = area4;
        }
    }

    public static class Area {
        @SerializedName("name")
        private String name;

        @SerializedName("coords")
        private Coords coords;

        @SerializedName("alias")
        private String alias;

        // Getters and Setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coords getCoords() {
            return coords;
        }

        public void setCoords(Coords coords) {
            this.coords = coords;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }

    public static class Coords {
        @SerializedName("center")
        private Center center;

        // Getters and Setters

        public Center getCenter() {
            return center;
        }

        public void setCenter(Center center) {
            this.center = center;
        }
    }

    public static class Center {
        @SerializedName("crs")
        private String crs;

        @SerializedName("x")
        private double x;

        @SerializedName("y")
        private double y;

        // Getters and Setters

        public String getCrs() {
            return crs;
        }

        public void setCrs(String crs) {
            this.crs = crs;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
