package com.ua.monitoring.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.ua.monitoring.dto.MonitoredEndpointRequestDto;
import com.ua.monitoring.dto.MonitoredEndpointResponseDto;
import com.ua.monitoring.model.MonitoredEndpoint;
import com.ua.monitoring.service.impl.SchedulerImpl;
import com.ua.monitoring.service.MonitoredEndpointService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("monitored-endpoint/")
@AllArgsConstructor
public class MonitoredEndpointController {

    private final WebClient.Builder webClientBuilder;
    private final MonitoredEndpointService monitoredEndpointService;
    private final ModelMapper modelMapper;
    private final SchedulerImpl schedulerImpl;

    @GetMapping("all")
    public ResponseEntity<List<MonitoredEndpointResponseDto>> getMonitoredEndpoints(
            @RequestHeader("accessToken") final String accessToken
    ) {
        final Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(monitoredEndpointService.getAllByUserId(getUserId(accessToken)).stream()
                .map(entity -> modelMapper.map(entity, MonitoredEndpointResponseDto.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("endpoints-ids")
    public ResponseEntity<List<Long>> endpointsIds(
            @RequestHeader("accessToken") final String accessToken,
            @RequestParam("url") final String url
    ) {
        final Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(monitoredEndpointService.findIdsByUserIdAndUrl(userId, url));
    }

    @PostMapping("{id}")
    public ResponseEntity<MonitoredEndpointResponseDto> edit(
            @RequestHeader("accessToken") final String accessToken,
            @PathVariable("id") final Long id,
            @RequestBody final MonitoredEndpointRequestDto monitoredDto
    ) {
        final Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }

        return monitoredEndpointService.findById(id)
                .map(endpoint -> {
                    endpoint.setName(monitoredDto.getName());
                    endpoint.setUrl(monitoredDto.getUrl());
                    endpoint.setMonitoredInterval(monitoredDto.getMonitoredInterval());
                    return monitoredEndpointService.saveOrUpdate(endpoint);
                })
                .stream().peek(endpoint -> {
                    schedulerImpl.removeMonitoringJob(endpoint.getId());
                    schedulerImpl.addMonitoringJob(endpoint);
                })
                .findFirst()
                .map(endpoint -> modelMapper.map(endpoint, MonitoredEndpointResponseDto.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("create")
    public ResponseEntity<MonitoredEndpointResponseDto> create(
            @RequestHeader("accessToken") final String accessToken,
            @RequestBody final MonitoredEndpointRequestDto monitoredDto
    ) {
        Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }

        final MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setName(monitoredDto.getName());
        monitoredEndpoint.setUrl(monitoredDto.getUrl());
        monitoredEndpoint.setMonitoredInterval(monitoredDto.getMonitoredInterval());
        monitoredEndpoint.setDateOfCreation(LocalDateTime.now());
        monitoredEndpoint.setUserId(userId);

        final MonitoredEndpoint endpoint = monitoredEndpointService.saveOrUpdate(monitoredEndpoint);
        schedulerImpl.addMonitoringJob(endpoint);
        return ResponseEntity.ok(modelMapper.map(endpoint, MonitoredEndpointResponseDto.class));
    }

    @DeleteMapping("delete/{endpointId}")
    public ResponseEntity<HttpStatus> remove(
            @RequestHeader("accessToken") final String accessToken,
            @PathVariable("endpointId") final Long endpointId
    ) {
        monitoredEndpointService.deleteByIdAndUserId(endpointId, getUserId(accessToken));
        schedulerImpl.removeMonitoringJob(endpointId);
        return ResponseEntity.ok().build();
    }

    private Long getUserId(final String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service/users/get-id")
                .header("accessToken", accessToken)
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        return Mono.just(0L);
                    }
                    return response.bodyToMono(Long.class);
                })
                .block();
    }
}
