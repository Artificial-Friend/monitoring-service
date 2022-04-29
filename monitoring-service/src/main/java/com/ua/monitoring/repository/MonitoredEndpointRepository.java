package com.ua.monitoring.repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ua.monitoring.model.MonitoredEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {

    @Override
    Optional<MonitoredEndpoint> findById(Long aLong);

    List<MonitoredEndpoint> findAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE MonitoredEndpoint SET dateOfLastCheck = ?1 WHERE id = ?2 ")
    void updateLastCheck(LocalDateTime time, Long id);
}
