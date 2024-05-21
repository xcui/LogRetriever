package com.example.logretriever.controller;

import com.example.logretriever.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class LogControllerTest {

    @Mock
    private LogService logService;

    private LogController logController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logController = new LogController(logService);
    }

    @Test
    void testGetLogsHappyPath() throws IOException, NoSuchFieldException, IllegalAccessException {
        final String filename = "example.log";
        final List<String> expectedLogs = List.of("log1", "log2", "log3");
        when(logService.getLogEntries(Paths.get(getLogPath(), filename), eq(0), eq(null))).thenReturn(expectedLogs);

        final List<String> logs = logController.getLogs(filename, 0, null);

        assertEquals(expectedLogs, logs);
    }

    @Test
    void testGetLogsWithNumberOfLines() throws IOException {
        String filename = "test.log";
        int numberOfLines = 2;
        List<String> expectedLogs = Arrays.asList("log2", "log3");
        when(logService.getLogEntries(any(), eq(numberOfLines), eq(null))).thenReturn(expectedLogs);

        List<String> actualLogs = logController.getLogs(filename, numberOfLines, null);

        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    void testGetLogsWithFilter() throws IOException {
        String filename = "test.log";
        String filter = "error";
        List<String> expectedLogs = Arrays.asList("log1 error", "log3 error");
        when(logService.getLogEntries(any(), eq(0), eq(filter))).thenReturn(expectedLogs);

        List<String> actualLogs = logController.getLogs(filename, 0, filter);

        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    void testGetLogsWithNumberOfLinesAndFilter() throws IOException {
        String filename = "test.log";
        int numberOfLines = 2;
        String filter = "error";
        List<String> expectedLogs = Arrays.asList("log3 error");
        when(logService.getLogEntries(any(), eq(numberOfLines), eq(filter))).thenReturn(expectedLogs);

        List<String> actualLogs = logController.getLogs(filename, numberOfLines, filter);

        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    void testGetLogsFileNotFound() throws IOException {
        String filename = "nonexistent.log";
        when(logService.getLogEntries(any(), eq(0), eq(null))).thenThrow(new IOException("File not found"));

        try {
            logController.getLogs(filename, 0, null);
        } catch (IOException e) {
            assertEquals("File not found", e.getMessage());
        }
    }

    private String getLogPath() throws NoSuchFieldException, IllegalAccessException {
        final Field field = LogController.class.getDeclaredField("LOG_PATH");
        field.setAccessible(true);
        return (String) field.get(null);
    }
}
