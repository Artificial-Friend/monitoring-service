package com.ua.monitoring.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MonitoredEndpointResponseDto {
    private Long id;
    private String name;
    private String url;
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfLastCheck;
    private Integer monitoredInterval;
}
