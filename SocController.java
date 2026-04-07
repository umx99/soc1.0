package com.soc.siem.controller;

import com.soc.siem.model.LogEntry;
import com.soc.siem.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*") // Allows your frontend to talk to the backend
public class SocController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public List<LogEntry> getAllLogs() {
        return logRepository.findAll();
    }
}