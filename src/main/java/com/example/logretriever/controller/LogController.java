package com.example.logretriever.controller;

import com.example.logretriever.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@AllArgsConstructor
public class LogController {

    private static final String LOG_PATH = "/var/log";

    @Autowired
    private LogService logService;

    /**
     * Method to handle GET requests.
     * @param filename filename for log file
     * @return list of log lines
     * @throws IOException
     */
    // TODO add support for number of read lines and text/keyword matches
    @GetMapping("/logs")
    public List<String> getLogs(
            @RequestParam final String filename,
            @RequestParam(defaultValue = "0") final int numberOfLines,
            @RequestParam(required = false) final String filter) throws IOException {
        return logService.getLogEntries(Paths.get(LOG_PATH, filename), numberOfLines, filter);
    }
}
