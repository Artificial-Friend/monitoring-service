package com.ua.monitoring.result.service.impl;

import java.util.List;

import com.ua.monitoring.result.model.MonitoringResult;
import com.ua.monitoring.result.repository.MonitoringResultRepository;
import com.ua.monitoring.result.service.MonitoringResultService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MonitoringResultServiceImpl implements MonitoringResultService {
    private final MonitoringResultRepository monitoringResultRepository;

    @Override
    public MonitoringResult create(final MonitoringResult monitoringResult) {
        return monitoringResultRepository.save(monitoringResult);
    }

    @Override
    public List<MonitoringResult> getLastTenByUrlAndUserId(final List<Long> endpointIds) {
        return monitoringResultRepository.findByUrlAndUserId(endpointIds, PageRequest.of(0, 10));
    }
}
