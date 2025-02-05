package com.ibm.service;

import com.ibm.exception.FileProcessingException;
import com.ibm.service.impl.FileReaderServiceImpl;
import com.ibm.service.impl.TracingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TracingServiceImplTest {

    private TracingService tracingService;

    @BeforeEach
    void setUp() throws FileProcessingException {

        FileReaderService fileReaderService = new FileReaderServiceImpl();
        fileReaderService.readGraphFromFile("src/test/resources/test_input.txt");
        tracingService = new TracingServiceImpl();
    }

    @Test
    void testFindAverageLatency_ValidTrace() {
        List<String> trace = Arrays.asList("A", "B", "C");
        Integer result = tracingService.findAverageLatency(trace);
        assertEquals(9, result);
    }

    @Test
    void testFindAverageLatency_InvalidTrace() {
        List<String> trace = Arrays.asList("A", "D", "F");
        Integer result = tracingService.findAverageLatency(trace);
        assertEquals(-1, result);
    }

    @Test
    void testFindTracePathByMaximumHops() {
        Integer result = tracingService.findTracePathByMaximumHops("C", "C", 3);
        assertEquals(2, result);
    }

    @Test
    void testFindTracePathByFixedHops() {
        Integer result = tracingService.findTracePathByFixedHops("A", "C", 4);
        assertEquals(3, result);
    }

    @ParameterizedTest
    @CsvSource({
            "'A', 'C', 9",
            "'A', 'F', -1"
    })
    void testFindShortestTrace(String start, String end, int expected) {
        Integer result = tracingService.findShortestTrace(start, end);
        assertEquals(expected, result);
    }

    @Test
    void testFindTracePathByMaxLatency() {
        Integer result = tracingService.findTracePathByMaxLatency("C", "C", 30);
        assertEquals(7, result);
    }
}
