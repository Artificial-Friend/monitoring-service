package com.ua.monitoring.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.ua.monitoring.dto.MonitoringResultResponseDto;
import com.ua.monitoring.service.MonitoringResultService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("monitoring-result/")
@AllArgsConstructor
public class MonitoringResultController {
    private final WebClient.Builder webClientBuilder;
    private final MonitoringResultService monitoringResultService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<MonitoringResultResponseDto>> getTenLastResults(
            @RequestHeader("accessToken") final String accessToken,
            @RequestParam("url") final String url) {
        Long userId = getUserId(accessToken);
        if (userId == 0) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(monitoringResultService.getLastTenByUrlAndUserId(url, userId).stream()
                .map(r -> modelMapper.map(r, MonitoringResultResponseDto.class))
                .collect(Collectors.toList()));
    }

    private Long getUserId(final String accessToken) {
        return webClientBuilder.build()
                .post()
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
