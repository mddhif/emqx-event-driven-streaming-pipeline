package com.telemetry.restapi.model;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Alert {


    private Long id;

    private String deviceId;

    private Timestamp timestamp;

    private double powerOutput;

    private String severity;

    private String message;

    private boolean acknowledged = false;
}