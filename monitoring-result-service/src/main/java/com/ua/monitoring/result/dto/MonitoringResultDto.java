package com.ua.monitoring.result.dto;

public record MonitoringResultDto(
        Long monitoredEndpointId,
        String returnedPayload,
        Integer returnedHttpStatusCode) {
}
