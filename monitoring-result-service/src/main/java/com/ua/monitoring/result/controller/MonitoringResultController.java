package com.ua.monitoring.result.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ua.monitoring.result.dto.MonitoringResultDto;
import com.ua.monitoring.result.dto.MonitoringResultResponseDto;
import com.ua.monitoring.result.model.MonitoringResult;
import com.ua.monitoring.result.service.MonitoringResultService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("monitoring-result/")
@AllArgsConstructor
public class MonitoringResultController {
    private final WebClient.Builder webClientBuilder;
    private final MonitoringResultService monitoringResultService;
    private final ModelMapper modelMapper;

    @PostMapping("create")
    public ResponseEntity<Void> create(
            @RequestHeader("accessToken") final String accessToken,
            @RequestBody final MonitoringResultDto monitoringResultDto
    ) {
        Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }
        monitoringResultService.create(convertResult(monitoringResultDto));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MonitoringResultResponseDto>> getTenLastResults(
            @RequestHeader("accessToken") final String accessToken,
            @RequestParam("url") final String url) {
        Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }

        final List<Long> endpointIds = webClientBuilder.baseUrl("http://monitoring-service/").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("monitored-endpoint/endpoints-ids")
                        .queryParam("url", url)
                        .build())
                .header("accessToken", accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .onErrorResume(WebClientResponseException.class, notFound -> Mono.empty())
                .block();

        return ResponseEntity.ok(monitoringResultService.getLastTenByUrlAndUserId(endpointIds).stream()
                .map(r -> modelMapper.map(r, MonitoringResultResponseDto.class))
                .collect(Collectors.toList()));
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

    private MonitoringResult convertResult(final MonitoringResultDto resultDto) {
        final MonitoringResult result = new MonitoringResult();
        result.setMonitoredEndpointId(resultDto.monitoredEndpointId());
        result.setDateOfCheck(LocalDateTime.now());
        result.setReturnedPayload(resultDto.returnedPayload());
        result.setReturnedHttpStatusCode(resultDto.returnedHttpStatusCode());

        return result;
    }
}
