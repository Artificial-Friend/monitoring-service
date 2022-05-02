package com.ua.monitoring.result.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import lombok.Data;

@Entity
@Table(name = "monitoring_results")
@Data
public class MonitoringResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateOfCheck;

    @Column(nullable = false)
    private Integer returnedHttpStatusCode;

    @Column(columnDefinition="LONGTEXT")
    private String returnedPayload;

    @Column(nullable = false)
    private Long monitoredEndpointId;
}
