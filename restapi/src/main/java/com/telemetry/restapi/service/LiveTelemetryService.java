package com.telemetry.restapi.service;


import com.telemetry.restapi.model.Alert;
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
    private final AlertStream alertStream;

    @KafkaListener(topics = "telemetry.live", groupId = "restapi-service", properties = {
            "spring.json.value.default.type=com.telemetry.restapi.model.TelemetryData"
    })
    public void consumeTelemetry(TelemetryData telemetryData) {

        log.info("Received live telemetry data: {}", telemetryData);
        telemetryStream.publish(telemetryData);

    }

    @KafkaListener(topics = "telemetry.alerts.live", groupId = "restapi-service", properties = {
            "spring.json.value.default.type=com.telemetry.restapi.model.Alert"
    })
    public void consumeAlerts(Alert alert) {

        log.info("Received live alert data: {}", alert);
        alertStream.publish(alert);

    }
}
