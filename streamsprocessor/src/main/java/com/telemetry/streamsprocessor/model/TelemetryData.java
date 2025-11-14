package com.telemetry.streamsprocessor.model;


import lombok.Data;

@Data
public class TelemetryData {
    private String device_id;
    private String timestamp;
    private double power_output;
    private double temperature;
    private String status;
    private String flag;
}