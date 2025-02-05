package com.ibm.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository class for managing graph data.
 */
public class GraphRepository {
    static Map<String, Map<String, Integer>> graphData = new HashMap<>();

    /**
     * Adds an edge to the graph with the specified latency.
     *
     * @param from    The starting node of the edge.
     * @param to      The ending node of the edge.
     * @param latency The latency of the edge.
     */
    public void addEdge(String from, String to, int latency) {
        graphData.computeIfAbsent(from, k -> new HashMap<>()).put(to, latency);
    }

    /**
     * Returns the graph data.
     *
     * @return A map representing the graph data.
     */
    public static Map<String, Map<String, Integer>> graphData() {
        return graphData;
    }
}

