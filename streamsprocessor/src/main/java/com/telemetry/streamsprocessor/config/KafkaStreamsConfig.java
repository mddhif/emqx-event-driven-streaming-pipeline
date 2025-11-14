package com.telemetry.streamsprocessor.config;


import com.telemetry.streamsprocessor.processor.StreamsProcessor;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<String, String> telemetryStream(StreamsBuilder builder, StreamsProcessor processor) {
        return processor.buildPipeline(builder);
    }
}