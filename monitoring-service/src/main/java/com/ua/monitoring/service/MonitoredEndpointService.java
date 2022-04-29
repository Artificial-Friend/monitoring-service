package com.ua.monitoring.service;

import java.util.List;
import java.util.Optional;

import com.ua.monitoring.model.MonitoredEndpoint;

public interface MonitoredEndpointService {

    Optional<MonitoredEndpoint> findById(Long id);

    List<MonitoredEndpoint> getAllByUserId(Long userId);

    MonitoredEndpoint saveOrUpdate(MonitoredEndpoint monitoredEndpoint);

    void deleteByIdAndUserId(Long endpointId, Long userId);

    void updateLastCheck(MonitoredEndpoint monitoredEndpoint);
}
