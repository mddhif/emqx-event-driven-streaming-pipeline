package com.telemetry.restapi.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TelemetryData {

    private String device_id;
    private Timestamp timestamp;
    private double power_output;
    private double temperature;
    private String status;
    private String flag;
}
