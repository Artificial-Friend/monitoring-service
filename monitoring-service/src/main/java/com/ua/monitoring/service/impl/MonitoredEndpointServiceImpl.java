package com.ua.monitoring.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ua.monitoring.model.MonitoredEndpoint;
import com.ua.monitoring.repository.MonitoredEndpointRepository;
import com.ua.monitoring.service.MonitoredEndpointService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MonitoredEndpointServiceImpl implements MonitoredEndpointService {
    private final MonitoredEndpointRepository monitoredEndpointRepository;

    @Override
    public MonitoredEndpoint saveOrUpdate(final MonitoredEndpoint monitoredEndpoint) {
        return monitoredEndpointRepository.save(monitoredEndpoint);
    }

    @Override
    public Optional<MonitoredEndpoint> findById(final Long id) {
        return monitoredEndpointRepository.findById(id);
    }

    @Override
    public List<MonitoredEndpoint> getAllByUserId(final Long userId) {
        return monitoredEndpointRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteByIdAndUserId(final Long endpointId, final Long userId) {
        monitoredEndpointRepository.findById(endpointId)
                .filter(e -> e.getUserId().equals(userId))
                .ifPresent(monitoredEndpointRepository::delete);
    }

    @Override
    public void updateLastCheck(final MonitoredEndpoint monitoredEndpoint) {
        monitoredEndpoint.setDateOfLastCheck(LocalDateTime.now());
        monitoredEndpointRepository.updateLastCheck(monitoredEndpoint.getDateOfLastCheck(), monitoredEndpoint.getId());
    }
}
