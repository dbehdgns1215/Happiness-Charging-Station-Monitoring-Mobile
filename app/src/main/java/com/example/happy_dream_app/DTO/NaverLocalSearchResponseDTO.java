package com.example.happy_dream_app.DTO;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NaverLocalSearchResponseDTO {
    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @SerializedName("total")
    private int total;

    @SerializedName("start")
    private int start;

    @SerializedName("display")
    private int display;

    @SerializedName("items")
    private List<Item> items;

    // Getters and Setters

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        @SerializedName("title")
        private String title;

        @SerializedName("link")
        private String link;

        @SerializedName("category")
        private String category;

        @SerializedName("description")
        private String description;

        @SerializedName("telephone")
        private String telephone;

        @SerializedName("address")
        private String address;

        @SerializedName("roadAddress")
        private String roadAddress;

        @SerializedName("mapx")
        private double mapx;

        @SerializedName("mapy")
        private double mapy;

        // Getters and Setters

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRoadAddress() {
            return roadAddress;
        }

        public void setRoadAddress(String roadAddress) {
            this.roadAddress = roadAddress;
        }

        public double getMapx() {
            return mapx;
        }

        public void setMapx(double mapx) {
            this.mapx = mapx;
        }

        public double getMapy() {
            return mapy;
        }

        public void setMapy(double mapy) {
            this.mapy = mapy;
        }
    }
}
