package com.telemetry.restapi.service;

import com.telemetry.restapi.model.TelemetryData;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class TelemetryStream {

    private final Sinks.Many<TelemetryData> sink =
            Sinks.many().multicast().onBackpressureBuffer();

    @Getter
    private final Flux<TelemetryData> flux = sink.asFlux();

    public void publish(TelemetryData data) {
        sink.tryEmitNext(data);
    }

}
