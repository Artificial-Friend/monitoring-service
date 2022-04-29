package com.ua.monitoring.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MonitoredEndpointRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    @NotNull
    @Min(10)
    private Integer monitoredInterval;
}
