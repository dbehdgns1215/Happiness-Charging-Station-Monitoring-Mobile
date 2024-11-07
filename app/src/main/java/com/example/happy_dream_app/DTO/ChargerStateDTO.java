package com.example.happy_dream_app.DTO;

import java.time.LocalDateTime;

public class ChargerStateDTO {
    // 충전 상태 기본 데이터
    private Integer id;
    private Integer chargerId;
    private Boolean usingYn;
    private Boolean brokenYn;

    // 관리 목적 데이터
    private LocalDateTime usingAt;
    private LocalDateTime brokenAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
