package com.telemetry.alert.service;


import com.telemetry.alert.model.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertService {


    @KafkaListener(topics = "telemetry.stored", groupId = "alert-service")
    public void handleAlert(Alert alert) {

        log.info("Received message: {}", alert);
    }
}
