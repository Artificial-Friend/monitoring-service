package com.ua.monitoring.service;

import com.ua.monitoring.model.MonitoredEndpoint;

public interface Scheduler {
	void addMonitoringJob(MonitoredEndpoint monitoredEndpoint);

	void removeMonitoringJob(Long monitoredEndpointId);
}
