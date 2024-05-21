package com.example.logretriever.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void SetUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleFileNotFoundException() {
        final FileNotFoundException ex = new FileNotFoundException("test.log not found");

        final ErrorResponse response = globalExceptionHandler.handleFileNotFoundException(ex).getBody();

        assertEquals("File not found: test.log not found", response.getMessage());
        assertEquals(404, response.getStatus());
    }

    @Test
    void testHandleIOException() {
        final IOException ex = new IOException("I/O error");

        final ErrorResponse response = globalExceptionHandler.handleIOException(ex).getBody();

        assertEquals("Error reading file: I/O error", response.getMessage());
        assertEquals(500, response.getStatus());
    }

    @Test
    void testHandleException() {
        final Exception ex = new Exception("Generic error");

        final ErrorResponse response = globalExceptionHandler.handleException(ex).getBody();

        assertEquals("Error: Generic error", response.getMessage());
        assertEquals(400, response.getStatus());
    }
}
