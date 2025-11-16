package com.telemetry.restapi.service;

import com.telemetry.restapi.common.stream.EventStream;
import com.telemetry.restapi.model.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertStream extends EventStream<Alert> {}
