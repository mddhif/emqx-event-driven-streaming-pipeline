package com.telemetry.storage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telemetry.storage.Config.RabbitConfig;
import com.telemetry.storage.model.Alert;
import com.telemetry.storage.model.TelemetryData;
import com.telemetry.storage.repository.AlertRepository;
import com.telemetry.storage.repository.TelemetryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StorageService {

       private final ObjectMapper objectMapper = new ObjectMapper();
       private final TelemetryRepository telemetryRepository;
       private final AlertRepository alertRepository;
       private final KafkaTemplate<String, Object> kafkaTemplate;
       private final RabbitTemplate rabbitTemplate;

       public StorageService(TelemetryRepository telemetryRepository,
                             AlertRepository alertRepository,
                             KafkaTemplate<String, Object> kafkaTemplate,
                             RabbitTemplate rabbitTemplate) {
        this.telemetryRepository = telemetryRepository;
        this.alertRepository = alertRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.rabbitTemplate = rabbitTemplate;
       }

    @KafkaListener(topics = "telemetry.processed", groupId = "storage-service")
    public void handleTelemetry(String msg) {
        try {
            TelemetryData telemetryData = objectMapper.readValue(msg, TelemetryData.class);
            log.info("telemetry received: {}", msg);
            log.info("Saving to cassandra DB ...");
            telemetryRepository.save(telemetryData);
            // feed live stream (for the rest api service)
            publishToLiveStream("telemetry.live", telemetryData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "telemetry.alerts", groupId = "storage-service")
    public void handleAlert(String msg) {
        try {
            TelemetryData telemetryData = objectMapper.readValue(msg, TelemetryData.class);
            log.info("alert received: {}", msg);

            Alert alert = Alert.builder()
                    .deviceId(telemetryData.getDevice_id())
                    .timestamp(telemetryData.getTimestamp())
                    .powerOutput(telemetryData.getPower_output())
                    .severity(telemetryData.getFlag())
                    .message("Power output below 100W")
                    .build();

            log.info("Saving to postgresql DB ...");
            alertRepository.save(alert);
            log.info("Publishing to alert stored topic ...");

            // let alert service know
            //sendAlert("telemetry.stored", alert);
            publishAlertToRabbit(alert);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void sendAlert(String topic, Alert alert) {
           log.info("Sending alert to topic {}", topic);
           kafkaTemplate.send(topic, alert);
    }

    public void publishAlertToRabbit(Alert alert) {

           log.info("Publishing Alert ...");
            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.ROUTING_KEY,
                    alert
            );
    }

    public void publishToLiveStream(String topic, TelemetryData telemetryData) {
           log.info("Publishing to topic {}", topic);
           kafkaTemplate.send(topic, telemetryData);
    }
}
