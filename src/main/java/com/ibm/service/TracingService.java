package com.ibm.service;

import java.util.List;

/**
 * Interface for a service that provides tracing functionalities on a graph.
 */
public interface TracingService {

    /**
     * Finds the average latency of a given trace.
     *
     * @param nodes List of nodes representing the trace.
     * @return The average latency or -1 if the trace is invalid.
     */
    Integer findAverageLatency(List<String> nodes);

    /**
     * Finds the number of trace paths between two nodes with a maximum number of hops.
     *
     * @param start   The starting node.
     * @param end     The ending node.
     * @param maxHops The maximum number of hops.
     * @return The number of trace paths.
     */
    Integer findTracePathByMaximumHops(String start, String end, int maxHops);

    /**
     * Finds the number of trace paths between two nodes with a fixed number of hops.
     *
     * @param start     The starting node.
     * @param end       The ending node.
     * @param fixedHops The fixed number of hops.
     * @return The number of trace paths.
     */
    Integer findTracePathByFixedHops(String start, String end, int fixedHops);

    /**
     * Finds the number of trace paths between two nodes with a maximum latency.
     *
     * @param start      The starting node.
     * @param end        The ending node.
     * @param maxLatency The maximum latency.
     * @return The number of trace paths.
     */
    Integer findTracePathByMaxLatency(String start, String end, int maxLatency);

    /**
     * Finds the shortest trace between two nodes.
     *
     * @param start The starting node.
     * @param end   The ending node.
     * @return The latency of the shortest trace or -1 if no valid trace exists.
     */
    Integer findShortestTrace(String start, String end);
}
