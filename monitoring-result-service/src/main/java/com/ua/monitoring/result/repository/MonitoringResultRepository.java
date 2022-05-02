package com.ua.monitoring.result.repository;

import java.util.List;

import com.ua.monitoring.result.model.MonitoringResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {

    @Query("FROM MonitoringResult r " +
            "WHERE r.monitoredEndpointId IN ?1 " +
            "ORDER BY r.id ")
    List<MonitoringResult> findByUrlAndUserId(List<Long> endpointIds, Pageable pageable);
}
