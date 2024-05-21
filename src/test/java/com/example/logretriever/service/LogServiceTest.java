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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        final List<String> result = logService.getLogEntries(file.toPath());

        assertEquals(List.of("line3", "line2", "line1"), result);
    }

    @Test
    void testGetLogEntriesWithEmptyFile() throws IOException {
        final File file = createTempFileWithContent("");

        final List<String> result = logService.getLogEntries(file.toPath());

        assertTrue(result.isEmpty());
    }

    private File createTempFileWithContent(String content) throws IOException {
        final File file = tempDir.resolve("test.log").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }
}
