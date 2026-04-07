package com.soc.siem.service;

import com.soc.siem.model.SyslogMessage;
import com.soc.siem.util.LogParser;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class UdpIngestionService {

    private static final int PRIMARY_PORT = 514;
    private static final int FALLBACK_PORT = 10514; // Use this if 514 is blocked on Windows
    private static final int BUFFER_SIZE = 65535; // Max UDP packet size

    private DatagramSocket socket;
    private boolean running;
    // Dedicated thread pool for the listener to prevent blocking the main Spring context
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startListener() {
        try {
            socket = new DatagramSocket(PRIMARY_PORT);
            log.info("SIEM: Listening on UDP port {}", PRIMARY_PORT);
        } catch (SocketException e) {
            log.warn("Could not bind to port {}. Attempting fallback port {}...", PRIMARY_PORT, FALLBACK_PORT);
            try {
                socket = new DatagramSocket(FALLBACK_PORT);
                log.info("SIEM: Listening on fallback UDP port {}", FALLBACK_PORT);
            } catch (SocketException ex) {
                log.error("Failed to bind to any port. Ingestion service offline.", ex);
                return;
            }
        }

        running = true;
        executor.execute(this::listen);
    }

    private void listen() {
        byte[] buffer = new byte[BUFFER_SIZE];
        log.info("Ingestion loop started.");

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // Blocking call

                // Offload parsing and logging
                SyslogMessage message = LogParser.parse(packet);
                processMessage(message);

            } catch (IOException e) {
                if (running) {
                    log.error("Error receiving UDP packet: {}", e.getMessage());
                }
            }
        }
    }

    private void processMessage(SyslogMessage msg) {
        // Outputting to console as requested for Phase 1
        log.info("[LOG INGESTED] Source: {} | Time: {} | Data: {}", 
                 msg.sourceIp(), msg.timestamp(), msg.rawContent());
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        executor.shutdownNow();
        log.info("UDP Ingestion Service stopped.");
    }
}