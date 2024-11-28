package com.example.happy_dream_app.DTO;

public class ReportRequest {

    private int chargerId;
    private String reportContent;

    public ReportRequest(int chargerId, String reportContent) {
        this.chargerId = chargerId;
        this.reportContent = reportContent;
    }
}

