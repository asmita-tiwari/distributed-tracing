package com.ibm.model;

/**
 * Record representing a node in a trace with a service and its associated latency.
 */
public record TraceNode(String service, int latency) {
}

