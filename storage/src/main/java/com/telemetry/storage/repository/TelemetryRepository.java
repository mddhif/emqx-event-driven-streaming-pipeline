package com.telemetry.storage.repository;


import com.telemetry.storage.model.TelemetryData;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface TelemetryRepository extends CassandraRepository<TelemetryData, String> {
}