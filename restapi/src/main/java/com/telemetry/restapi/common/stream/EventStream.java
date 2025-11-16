package com.telemetry.restapi.common.stream;


import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class EventStream<T> {

    private final Sinks.Many<T> sink = Sinks.many().multicast().onBackpressureBuffer();
    @Getter
    private final Flux<T> flux = sink.asFlux();
    public void publish(T data) {
        sink.tryEmitNext(data);
    }
}
