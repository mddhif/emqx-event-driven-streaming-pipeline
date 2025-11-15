package com.telemetry.restapi.service;


import com.telemetry.restapi.model.TelemetryData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveTelemetryService {

    private final TelemetryStream telemetryStream;

    @KafkaListener(topics = "telemetry.live", groupId = "restapi-service")
    public void consume(TelemetryData telemetryData) {

        log.info("Received live telemetry data: {}", telemetryData);
        telemetryStream.publish(telemetryData);

    }
}
