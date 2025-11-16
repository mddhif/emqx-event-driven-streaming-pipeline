package com.telemetry.restapi.service;

import com.telemetry.restapi.common.stream.EventStream;
import com.telemetry.restapi.model.TelemetryData;
import org.springframework.stereotype.Service;


@Service
public class TelemetryStream extends EventStream<TelemetryData> {}
