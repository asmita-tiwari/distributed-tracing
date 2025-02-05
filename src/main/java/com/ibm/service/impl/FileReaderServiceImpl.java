package com.ibm.service.impl;

import com.ibm.exception.FileProcessingException;
import com.ibm.repository.GraphRepository;
import com.ibm.service.FileReaderService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Implementation of the FileReaderService interface.
 */
public class FileReaderServiceImpl implements FileReaderService {
    private final GraphRepository graphRepository;

    /**
     * Constructor that initializes the GraphRepository.
     */
    public FileReaderServiceImpl() {
        this.graphRepository = new GraphRepository();
    }

    /**
     * Reads graph data from a file and populates the GraphRepository.
     *
     * @param filename The name of the file containing the graph data.
     * @throws FileProcessingException If an error occurs while processing the file.
     */
    @Override
    public void readGraphFromFile(String filename) throws FileProcessingException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.lines()
                    .flatMap(line -> Arrays.stream(line.split(", ")))
                    .forEach(edge -> {
                        String sourceService = edge.substring(0, 1);
                        String destinationService = edge.substring(1, 2);
                        int latency = Integer.parseInt(edge.substring(2));
                        graphRepository.addEdge(sourceService, destinationService, latency);
                    });
        } catch (IOException e) {
            throw new FileProcessingException("File processing error: " + e);
        }
    }
}
