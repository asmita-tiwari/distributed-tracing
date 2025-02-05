package com.ibm.service;

import com.ibm.exception.FileProcessingException;
/**
 * Interface for a service that reads graph data from a file.
 */
public interface FileReaderService {

    /**
     * Reads graph data from the specified file and populates the graph repository.
     *
     * @param filename The name of the file containing the graph data.
     * @throws FileProcessingException If an error occurs while processing the file.
     */
    void readGraphFromFile(String filename) throws FileProcessingException;
}
