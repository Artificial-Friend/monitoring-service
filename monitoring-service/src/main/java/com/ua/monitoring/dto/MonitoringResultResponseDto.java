package com.ua.monitoring.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MonitoringResultResponseDto {
    private LocalDateTime dateOfCheck;
    private int returnedHttpStatusCode;
    private String returnedPayload;
}
