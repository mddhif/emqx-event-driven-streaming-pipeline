package com.telemetry.restapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.telemetry.restapi.model.TelemetryData;
import com.telemetry.restapi.service.TelemetryStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestAPIController {

    private final TelemetryStream  telemetryStream;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/telemetry/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TelemetryData> streamTelemetry() {
        return telemetryStream.getFlux();
    }

    @GetMapping("/telemetry/{deviceId}")
    public List<TelemetryData> getTelemetry(@PathVariable String deviceId) {

        Set<String> results = redisTemplate.opsForZSet().range("telemetry:" + deviceId, 0, -1);

        return (results != null) ? results.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, TelemetryData.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList() :
                Collections.emptyList();

    }


}
