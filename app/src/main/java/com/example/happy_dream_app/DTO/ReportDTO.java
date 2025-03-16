package com.example.happy_dream_app.DTO;

public class ReportDTO {

    private int chargerId;
    private String reportContent;

    public ReportDTO(int chargerId, String reportContent) {
        this.chargerId = chargerId;
        this.reportContent = reportContent;
    }
}

