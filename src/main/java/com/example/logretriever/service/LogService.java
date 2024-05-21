package com.example.logretriever.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
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
    public List<String> getLogEntries(
            final Path filePath,
            final int numberOfLines,
            final String filter) throws IOException {
        if (numberOfLines < 0) {
            throw new IllegalArgumentException("Number of lines must be non-negative");
        }

        final List<String> logEntries = new ArrayList<>();
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            final long fileLength = file.length();
            if (fileLength == 0) {
                return logEntries; // Return empty list if the file is empty
            }

            final StringBuilder sb = new StringBuilder();
            long pointer = fileLength - 1;
            int lineCount = 0;

            file.seek(pointer);
            for (pointer = fileLength - 1; pointer >= 0; pointer--) {
                file.seek(pointer);
                int readByte = file.read();
                if (readByte == '\n' || pointer == 0) { // Handle the first line of file correctly
                    if (!sb.isEmpty() || pointer == 0) {
                        if (pointer == 0) {
                            sb.append((char) readByte);
                        }
                        sb.reverse();
                        final String line = sb.toString();
                        sb.setLength(0);

                        if (filter == null || line.contains(filter)) {
                            logEntries.add(line);
                            lineCount++;
                        }
                        if (numberOfLines > 0 && lineCount >= numberOfLines) {
                            break;
                        }
                    }
                } else {
                    sb.append((char) readByte);
                }
            }
        }

        return logEntries;
    }
}
