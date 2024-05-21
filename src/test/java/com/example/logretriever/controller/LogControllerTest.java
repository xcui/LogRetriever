package com.example.logretriever.controller;

import com.example.logretriever.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(logService.getLogEntries(Paths.get(getLogPath(), filename))).thenReturn(expectedLogs);

        final List<String> logs = logController.getLogs(filename);

        assertEquals(expectedLogs, logs);
    }

    private String getLogPath() throws NoSuchFieldException, IllegalAccessException {
        final Field field = LogController.class.getDeclaredField("LOG_PATH");
        field.setAccessible(true);
        return (String) field.get(null);
    }
}
