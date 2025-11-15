package com.telemetry.restapi.controller;


import com.telemetry.restapi.model.TelemetryData;
import com.telemetry.restapi.service.TelemetryStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestAPIController {

    private final TelemetryStream  telemetryStream;

    @GetMapping(value = "/telemetry/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TelemetryData> streamTelemetry() {
        return telemetryStream.getFlux();
    }


}
