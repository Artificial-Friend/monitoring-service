package com.ua.monitoring.dto;

public record MonitoringResultDto(
        Long monitoredEndpointId,
        String returnedPayload,
        Integer returnedHttpStatusCode) {
}
