package com.ua.monitoring.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.ua.monitoring.dto.MonitoringResultDto;
import com.ua.monitoring.model.MonitoredEndpoint;
import com.ua.monitoring.service.MonitoredEndpointService;
import com.ua.monitoring.service.Scheduler;
import io.vertx.core.AbstractVerticle;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SchedulerImpl extends AbstractVerticle implements Scheduler {
    private final WebClient webClient;
    private final MonitoredEndpointService monitoredEndpointService;
    private final Map<Long, Long> endpointIdPeriodicIdMap = new HashMap<>();

    public SchedulerImpl(
            final WebClient webClient,
            final MonitoredEndpointService monitoredEndpointService
    ) {
        this.webClient = webClient;
        this.monitoredEndpointService = monitoredEndpointService;
    }

    @Override
    public void addMonitoringJob(final MonitoredEndpoint monitoredEndpoint) {
        if (endpointIdPeriodicIdMap.containsKey(monitoredEndpoint.getId())) {
            vertx.cancelTimer(endpointIdPeriodicIdMap.get(monitoredEndpoint.getId()));
        }

        if (monitoredEndpoint.getDateOfLastCheck() == null
                || monitoredEndpoint.getDateOfLastCheck().isBefore(
                monitoredEndpoint.getDateOfLastCheck().plusSeconds(
                        monitoredEndpoint.getMonitoredInterval()))) {
            createMonitoredResult(monitoredEndpoint);
        }
    }

    @Override
    public void removeMonitoringJob(final Long monitoredEndpointId) {
        if (!endpointIdPeriodicIdMap.containsKey(monitoredEndpointId)) {
            return;
        }

        vertx.cancelTimer(endpointIdPeriodicIdMap.get(monitoredEndpointId));
        endpointIdPeriodicIdMap.remove(monitoredEndpointId);
    }

    private void createMonitoredResult(final MonitoredEndpoint monitoredEndpoint) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(monitoredEndpoint.getUrl()))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            createResult(new MonitoringResultDto(monitoredEndpoint.getId(), response.body(), response.statusCode()));
        } catch (Exception e) {
            createResult(new MonitoringResultDto(monitoredEndpoint.getId(), e.getMessage(), 9999));
        }

        monitoredEndpointService.updateLastCheck(monitoredEndpoint);

        schedule(monitoredEndpoint);
    }

    private void schedule(final MonitoredEndpoint monitoredEndpoint) {
        endpointIdPeriodicIdMap.put(monitoredEndpoint.getId(),
                vertx.setTimer(monitoredEndpoint.getMonitoredInterval() * 1000,
                e -> createMonitoredResult(monitoredEndpoint)));
    }

    private void createResult(final MonitoringResultDto monitoringResultDto) {
        webClient.post()
                .uri("http://monitoring-result-service/monitoring-result/create")
                .header("accessToken", "777-joker-777")
                .body(BodyInserters.fromValue(monitoringResultDto))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
