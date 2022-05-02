package com.ua.monitoring.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.ua.monitoring.dto.MonitoringResultDto;
import com.ua.monitoring.model.MonitoredEndpoint;
import com.ua.monitoring.service.MonitoredEndpointService;
import com.ua.monitoring.service.Scheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SchedulerImpl implements SchedulingConfigurer, Scheduler {

    private final ScheduledTaskRegistrar scheduledTaskRegistrar;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final WebClient webClient;
    private final MonitoredEndpointService monitoredEndpointService;

    private final Map<Long, ScheduledFuture<?>> futureMap = new HashMap<>();

    public SchedulerImpl(
            final ScheduledTaskRegistrar scheduledTaskRegistrar,
            final ThreadPoolTaskScheduler taskScheduler,
            final WebClient webClient,
            final MonitoredEndpointService monitoredEndpointService
    ) {
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
        this.taskScheduler = taskScheduler;
        this.webClient = webClient;
        this.monitoredEndpointService = monitoredEndpointService;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar.getScheduler() == null) {
            taskRegistrar.setScheduler(taskScheduler);
        }
    }

    @Override
    public void addMonitoringJob(MonitoredEndpoint monitoredEndpoint) {
        if (futureMap.containsKey(monitoredEndpoint.getId())) {
            return;
        }

        if (monitoredEndpoint.getDateOfLastCheck() == null
                || monitoredEndpoint.getDateOfLastCheck().isBefore(
                monitoredEndpoint.getDateOfLastCheck().plusSeconds(
                        monitoredEndpoint.getMonitoredInterval()))) {
            createMonitoredResult(monitoredEndpoint);
        }

        ScheduledFuture<?> future = scheduledTaskRegistrar.getScheduler().schedule(
                () -> createMonitoredResult(monitoredEndpoint),
                t -> {
                    Calendar nextExecutionTime = new GregorianCalendar();

                    nextExecutionTime.setTime(Date.from(
                            monitoredEndpoint.getDateOfLastCheck().atZone(ZoneId.systemDefault()).toInstant()));
                    nextExecutionTime.add(Calendar.SECOND, monitoredEndpoint.getMonitoredInterval());
                    return nextExecutionTime.getTime();
                });

        configureTasks(scheduledTaskRegistrar);
        futureMap.put(monitoredEndpoint.getId(), future);
    }

    @Override
    public void removeMonitoringJob(final Long monitoredEndpointId) {
        if (!futureMap.containsKey(monitoredEndpointId)) {
            return;
        }
        ScheduledFuture<?> future = futureMap.get(monitoredEndpointId);
        future.cancel(true);
        futureMap.remove(monitoredEndpointId);
    }

    private void createMonitoredResult(MonitoredEndpoint monitoredEndpoint) {
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
