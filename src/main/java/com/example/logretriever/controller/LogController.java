package com.example.logretriever.controller;

import com.example.logretriever.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class for log controller that routes http requests for log related APIs.
 */
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @Value("${log.path:/var/log}")
    private String logPath;

    public LogController(final LogService logService) {
        this.logService = logService;
    }

    /**
     * Method to handle GET requests.
     * @param filename filename for log file
     * @return list of log lines
     * @throws IOException
     */
    @GetMapping("/logs")
    public List<String> getLogs(
            @RequestParam final String filename,
            @RequestParam(defaultValue = "0") final int numberOfLines,
            @RequestParam(required = false) final String filter) throws IOException {
        return logService.getLogEntries(Paths.get(logPath, filename), numberOfLines, filter);
    }
}
