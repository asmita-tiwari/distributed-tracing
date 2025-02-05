package com.ibm;

import com.ibm.exception.FileProcessingException;
import com.ibm.service.FileReaderService;
import com.ibm.service.TracingService;
import com.ibm.service.impl.FileReaderServiceImpl;
import com.ibm.service.impl.TracingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class DistributedTracing {

    private static final Logger logger = LoggerFactory.getLogger(DistributedTracing.class);

    public static void main(String[] args) throws FileProcessingException {
        processTracingServices("src/main/resources/input.txt");
    }

    /**
     * Processes tracing services by reading graph data from a file and logging the results.
     *
     * @param filename The name of the file containing the graph data.
     */

    public static void processTracingServices(String filename) throws FileProcessingException {
        FileReaderService fileReaderService = new FileReaderServiceImpl();
        fileReaderService.readGraphFromFile(filename);

        TracingService tracingService = new TracingServiceImpl();
        logResult("Average Latency (A -> B -> C)", tracingService.findAverageLatency(Arrays.asList("A", "B", "C")));
        logResult("Average Latency (A -> D)", tracingService.findAverageLatency(Arrays.asList("A", "D")));
        logResult("Average Latency (A -> D -> C)", tracingService.findAverageLatency(Arrays.asList("A", "D", "C")));
        logResult("Average Latency (A -> E -> B -> C -> D)", tracingService.findAverageLatency(Arrays.asList("A", "E", "B", "C", "D")));
        logResult("Average Latency (A -> E -> D)", tracingService.findAverageLatency(Arrays.asList("A", "E", "D")));
        logResult("Trace Path by Maximum Hops (C -> C, max 3 hops)", tracingService.findTracePathByMaximumHops("C", "C", 3));
        logResult("Trace Path by Fixed Hops (A -> C, 4 hops)", tracingService.findTracePathByFixedHops("A", "C", 4));
        logResult("Shortest Trace (A -> C)", tracingService.findShortestTrace("A", "C"));
        logResult("Shortest Trace (B -> B)", tracingService.findShortestTrace("B", "B"));
        logResult("Trace Path by Max Latency (C -> C, max 30 latency)", tracingService.findTracePathByMaxLatency("C", "C", 30));
    }

    private static void logResult(String description, Integer result) {
        if (result == -1) {
            logger.info("{}: NO SUCH TRACE", description);
        } else {
            logger.info("{}: {}", description, result);
        }
    }

}


