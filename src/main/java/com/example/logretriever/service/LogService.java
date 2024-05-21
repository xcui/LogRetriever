package com.example.logretriever.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for log service that handles core logic to query logs.
 */
@Service
public class LogService {

    /**
     * Method to get requested log lines. Newer log lines come first.
     * @param filePath full path for the requested log file
     * @return list of log lines
     * @throws IOException
     */
    // TODO add support for number of read lines and text/keyword matches
    public List<String> getLogEntries(final Path filePath) throws IOException {
        final List<String> logEntries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            final List<String> lines = reader.lines().toList();

            for (int i = lines.size() - 1; i >= 0; i--) {
                final String line = lines.get(i);

                logEntries.add(line);
            }
        }

        return logEntries;
    }
}
