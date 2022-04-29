package com.ua.monitoring.service.impl;

import java.util.List;

import com.ua.monitoring.model.MonitoringResult;
import com.ua.monitoring.repository.MonitoringResultRepository;
import com.ua.monitoring.service.MonitoringResultService;
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
    public List<MonitoringResult> getLastTenByUrlAndUserId(final String url, final Long userId) {
        return monitoringResultRepository.findByUrlAndUserId(url, userId, PageRequest.of(0, 10));
    }
}
