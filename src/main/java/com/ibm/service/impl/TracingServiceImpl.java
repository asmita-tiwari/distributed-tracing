package com.ibm.service.impl;

import com.ibm.model.TraceNode;
import com.ibm.repository.GraphRepository;
import com.ibm.service.TracingService;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Optional;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the TracingService interface.
 */
public class TracingServiceImpl implements TracingService {
    Map<String, Map<String, Integer>> graphData;

    /**
     * Constructor that initializes the graph data from the GraphRepository.
     */
    public TracingServiceImpl() {
        this.graphData = GraphRepository.graphData();
    }

    /**
     * Finds the average latency of a given trace.
     *
     * @param trace List of nodes representing the trace.
     * @return The average latency or -1 if the trace is invalid.
     */
    @Override
    public Integer findAverageLatency(List<String> trace) {
        return IntStream.range(0, trace.size() - 1)
                .mapToObj(i -> Map.entry(trace.get(i), trace.get(i + 1)))
                .map(entry -> Optional.ofNullable(graphData.get(entry.getKey()))
                        .map(destinations -> destinations.get(entry.getValue()))
                        .orElse(-1))
                .reduce((a, b) -> a == -1 || b == -1 ? -1 : a + b)
                .orElse(-1);
    }

    /**
     * Finds the number of trace paths between two nodes with a maximum number of hops.
     *
     * @param start   The starting node.
     * @param end     The ending node.
     * @param maxHops The maximum number of hops.
     * @return The number of trace paths.
     */
    @Override
    public Integer findTracePathByMaximumHops(String start, String end, int maxHops) {
        return countTraces(start, end, maxHops, false);
    }

    /**
     * Finds the number of trace paths between two nodes with a fixed number of hops.
     *
     * @param start     The starting node.
     * @param end       The ending node.
     * @param fixedHops The fixed number of hops.
     * @return The number of trace paths.
     */
    @Override
    public Integer findTracePathByFixedHops(String start, String end, int fixedHops) {
        return countTraces(start, end, fixedHops, true);
    }

    /**
     * Finds the shortest trace between two nodes.
     *
     * @param start The starting node.
     * @param end   The ending node.
     * @return The latency of the shortest trace or -1 if no valid trace exists.
     */
    @Override
    public Integer findShortestTrace(String start, String end) {
        if (!isValidTrace(start, end)) return -1;
        return findShortestTraceLatency(start, end);
    }

    /**
     * Finds the number of trace paths between two nodes with a maximum latency.
     *
     * @param start      The starting node.
     * @param end        The ending node.
     * @param maxLatency The maximum latency.
     * @return The number of trace paths.
     */
    @Override
    public Integer findTracePathByMaxLatency(String start, String end, int maxLatency) {
        AtomicInteger count = new AtomicInteger();
        countTracesWithMaxLatencyHelper(start, end, 0, 0, maxLatency, count);
        return count.get();
    }

    private void countTracesWithMaxLatencyHelper(String current, String end, int currentHops,
                                                 int currentLatency, int maxLatency, AtomicInteger count) {
        if (currentLatency >= maxLatency) {
            return;
        }
        if (current.equals(end) && currentHops > 0) {
            count.incrementAndGet();
        }
        graphData.getOrDefault(current, Map.of()).forEach((neighbor, latency) ->
                countTracesWithMaxLatencyHelper(neighbor, end, currentHops + 1, currentLatency + latency, maxLatency, count)
        );
    }

    public int countTraces(String start, String end, int hops, boolean fixedHops) {
        AtomicInteger count = new AtomicInteger();
        countTracesHelper(start, end, 0, hops, fixedHops, count);
        return count.get();
    }

    private void countTracesHelper(String current, String end, int currentHops, int targetHops, boolean fixedHops, AtomicInteger count) {
        if (currentHops > targetHops) {
            return;
        }
        if (current.equals(end) && (!fixedHops || currentHops == targetHops) && currentHops > 0) {
            count.incrementAndGet();
        }
        graphData.getOrDefault(current, Map.of()).keySet()
                .forEach(neighbor -> countTracesHelper(neighbor, end, currentHops + 1, targetHops, fixedHops, count));
    }

    private int findShortestTraceLatency(String start, String end) {
        PriorityQueue<TraceNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.latency()));
        Map<String, Integer> distances = initializeDistances();

        if (start.equals(end)) {
            for (Map.Entry<String, Integer> neighbor : graphData.get(start).entrySet()) {
                pq.add(new TraceNode(neighbor.getKey(), neighbor.getValue()));
            }
        } else {
            distances.put(start, 0);
            pq.add(new TraceNode(start, 0));
        }

        while (!pq.isEmpty()) {
            TraceNode current = pq.poll();

            if (current.service().equals(end) && (!start.equals(end) || current.latency() != 0)) {
                return current.latency();
            }

            checkNeighborsTraceNode(graphData, pq, distances, current);
        }
        return -1;
    }

    private void checkNeighborsTraceNode(
            Map<String, Map<String, Integer>> graphData,
            PriorityQueue<TraceNode> pq,
            Map<String, Integer> distances,
            TraceNode current) {
        if (graphData.containsKey(current.service())) {
            for (Map.Entry<String, Integer> neighbor : graphData.get(current.service()).entrySet()) {
                String neighborService = neighbor.getKey();
                int newLatency = current.latency() + neighbor.getValue();
                if (newLatency < distances.getOrDefault(neighborService, Integer.MAX_VALUE)) {
                    distances.put(neighborService, newLatency);
                    pq.add(new TraceNode(neighborService, newLatency));
                }
            }
        }
    }

    private boolean isValidTrace(String start, String end) {
        return graphData.containsKey(start) && graphData.containsKey(end);
    }

    private Map<String, Integer> initializeDistances() {
        return graphData.keySet().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        v -> Integer.MAX_VALUE
                ));
    }
}
