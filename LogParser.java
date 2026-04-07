package com.soc.siem.util;

import com.soc.siem.model.SyslogMessage;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class LogParser {

    public static SyslogMessage parse(DatagramPacket packet) {
        String content = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
        String ip = packet.getAddress().getHostAddress();
        
        // Simple extraction: Use current time as the arrival timestamp
        return new SyslogMessage(Instant.now(), ip, content.trim());
    }
}