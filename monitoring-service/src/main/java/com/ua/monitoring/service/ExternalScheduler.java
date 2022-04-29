package com.ua.monitoring.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.ua.monitoring.model.MonitoredEndpoint;
import com.ua.monitoring.model.MonitoringResult;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExternalScheduler implements SchedulingConfigurer {

    private ScheduledTaskRegistrar scheduledTaskRegistrar;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final MonitoringResultService monitoringResultService;
    private final MonitoredEndpointService monitoredEndpointService;

    private final Map<Long, ScheduledFuture<?>> futureMap = new HashMap<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (taskRegistrar.getScheduler() == null) {
            taskRegistrar.setScheduler(taskScheduler);
        }
    }

    public void addJob(MonitoredEndpoint monitoredEndpoint) {
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

    public void removeJob(final Long monitoredEndpointId) {
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
            monitoringResultService.create(
                    createResult(monitoredEndpoint.getId(), response.body(), response.statusCode()));
            monitoredEndpointService.updateLastCheck(monitoredEndpoint);
        } catch (Exception e) {
            monitoringResultService.create(
                    createResult(monitoredEndpoint.getId(), e.getMessage(), 9999));
        }
    }

    private MonitoringResult createResult(
            final Long monitoringEndpointId,
            final String payload,
            final int statusCode) {
        final MonitoringResult result = new MonitoringResult();
        result.setMonitoredEndpointId(monitoringEndpointId);
        result.setDateOfCheck(LocalDateTime.now());
        result.setReturnedPayload(payload);
        result.setReturnedHttpStatusCode(statusCode);

        return result;
    }
}
