package com.ua.monitoring.result.service;

import java.util.List;

import com.ua.monitoring.result.model.MonitoringResult;

public interface MonitoringResultService {
    MonitoringResult create(MonitoringResult monitoringResult);

    List<MonitoringResult> getLastTenByUrlAndUserId(List<Long> userId);
}
