package com.soc.siem.service;

import com.soc.siem.model.LogEntry;
import com.soc.siem.repository.LogRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Service
public class SyslogListener {

    @Autowired
    private LogRepository logRepository;

    @PostConstruct
    public void start() {
        new Thread(this::listen).start();
    }

    private void listen() {
        try (DatagramSocket socket = new DatagramSocket(10514)) {
            byte[] buffer = new byte[2048];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                String rawData = new String(packet.getData(), 0, packet.getLength());
                String ip = packet.getAddress().getHostAddress();

                // Simple logic: if message contains "attack", mark as CRITICAL
                String level = rawData.toLowerCase().contains("attack") ? "CRITICAL" : "INFO";

                LogEntry log = new LogEntry(ip, rawData, level);
                logRepository.save(log); // Persistent storage!
                
                System.out.println("Saved to DB: " + rawData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}