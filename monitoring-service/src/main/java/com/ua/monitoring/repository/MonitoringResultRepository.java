package com.ua.monitoring.repository;

import java.util.List;

import com.ua.monitoring.model.MonitoringResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {

    @Query("FROM MonitoringResult r " +
            "WHERE r.monitoredEndpointId = (SELECT e.id FROM MonitoredEndpoint e WHERE e.url = ?1 AND e.userId = ?2) " +
            "ORDER BY r.id ")
    List<MonitoringResult> findByUrlAndUserId(String url, Long userId, Pageable pageable);
}
