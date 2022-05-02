package com.ua.monitoring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

import lombok.Data;

@Entity
@Table(name = "monitored_endpoints")
@Data
public class MonitoredEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private LocalDateTime dateOfCreation;

    private LocalDateTime dateOfLastCheck;

    @Column(nullable = false)
    @Min(10)
    private Integer monitoredInterval;

    @Column(nullable = false)
    private Long userId;
}
