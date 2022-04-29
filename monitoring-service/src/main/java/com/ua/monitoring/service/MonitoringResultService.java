package com.ua.monitoring.service;

import java.util.List;

import com.ua.monitoring.model.MonitoringResult;

public interface MonitoringResultService {
    MonitoringResult create(MonitoringResult monitoringResult);

    List<MonitoringResult> getLastTenByUrlAndUserId(String url, Long userId);
}
