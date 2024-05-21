package com.example.logretriever.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogServiceTest {

    @TempDir
    static Path tempDir;

    @Mock
    private BufferedReader reader;

    private LogService logService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logService = new LogService();
    }

    @Test
    void testGetLogEntriesWithMultipleLines() throws IOException {
        final File file = createTempFileWithContent("line1\nline2\nline3");

        final List<String> result = logService.getLogEntries(file.toPath(), 3, null);

        assertEquals(List.of("line3", "line2", "line1"), result);
    }

    @Test
    void testGetLogEntriesWithEmptyFile() throws IOException {
        final File file = createTempFileWithContent("");

        final List<String> result = logService.getLogEntries(file.toPath(), 0, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLogEntriesWithNumberOfLines() throws IOException {
        final File logFile = createTempFileWithContent("test.log", "log1", "log2", "log3");
        final List<String> expectedLogs = List.of("log3", "log2");

        final List<String> actualLogs = logService.getLogEntries(logFile.toPath(), 2, null);

        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    void testGetLogEntriesWithFilter() throws IOException {
        final File logFile = createTempFileWithContent("test.log", "log1", "log2 error", "log3 error");
        final List<String> expectedLogs = List.of("log3 error", "log2 error");

        final List<String> actualLogs = logService.getLogEntries(logFile.toPath(), 0, "error");

        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    void testGetLogEntriesWithNumberOfLinesAndFilter() throws IOException {
        final File logFile = createTempFileWithContent("test.log", "log1", "log2 error", "log3 error");
        final List<String> expectedLogs = List.of("log3 error");

        final List<String> actualLogs = logService.getLogEntries(logFile.toPath(), 1, "error");

        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    void testGetLogEntriesFileNotFound() {
        final Path logFile = tempDir.resolve("nonexistent.log");

        final IOException exception = assertThrows(IOException.class, () -> {
            logService.getLogEntries(logFile, 0, null);
        });

        assertEquals("nonexistent.log", exception.getMessage());
    }

    private File createTempFileWithContent(final String... lines) throws IOException {
        final File file = tempDir.resolve("test.log").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            for (String line : lines) {
                writer.write(line);
            }
        }
        return file;
    }
}
