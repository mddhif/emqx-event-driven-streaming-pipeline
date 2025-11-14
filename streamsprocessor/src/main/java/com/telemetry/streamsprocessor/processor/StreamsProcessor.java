package com.telemetry.streamsprocessor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telemetry.streamsprocessor.model.TelemetryData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class StreamsProcessor {

    public final ObjectMapper mapper = new ObjectMapper();

    public KStream<String, String> buildPipeline(StreamsBuilder builder) {
        KStream<String, String> input = builder.stream("telemetry.raw");

        KStream<String, String> processedStream = input
                .mapValues(value -> {
                    try {
                        log.info("Parsing Telemetry Data ...");
                        TelemetryData data = mapper.readValue(value, TelemetryData.class);

                        String flag = data.getPower_output() < 100
                                ? "CRITICAL"
                                : data.getPower_output() < 1000
                                ? "WARNING"
                                : "NORMAL";
                        data.setFlag(flag);

                        log.info("Device {} | Power: {} | Flag: {}",
                                data.getDevice_id(), data.getPower_output(), flag);

                        return mapper.writeValueAsString(data);
                    } catch (Exception e) {
                        System.err.println("Invalid message: " + e.getMessage());
                        return value;
                    }
                });

        Map<String, KStream<String, String>> branches = processedStream
                .split(Named.as("telemetry-"))
                .branch(
                        (key, value) -> value.contains("\"flag\":\"CRITICAL\""),
                        Branched.as("alerts")
                )
                .defaultBranch(Branched.as("processed"));

        branches.get("telemetry-alerts").to("telemetry.alerts");
        branches.get("telemetry-processed").to("telemetry.processed");


        return branches.get("telemetry-processed");
    }
}

