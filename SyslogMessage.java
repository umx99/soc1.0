package com.soc.siem.model;

import java.time.Instant;

/**
 * Represents a structured log entry within the SOC backend.
 */
public record SyslogMessage(
    Instant timestamp,
    String sourceIp,
    String rawContent
) {}