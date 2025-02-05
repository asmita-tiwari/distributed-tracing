# Distributed Tracing

This project implements a distributed tracing system that reads graph data from a file and provides various tracing functionalities such as finding average latency, trace paths by hops, shortest trace, and trace paths by maximum latency.

## Project Structure

The project is organized into the following packages:

```
/project-root  
  ├── src/main/java/com/ibm  
  │   ├── exception  
  │   │   ├── FileProcessingException.java  # Custom exception class
  │   ├── model  
  │   │   ├── TraceNode.java  # Data model for tracing nodes
  │   ├── repository  
  │   │   ├── GraphRepository.java  # Repository for managing graph data
  │   ├── service  
  │   │   ├── FileReaderService.java  # Interface for file reading services
  │   │   ├── TracingService.java  # Interface for tracing services
  │   │   ├── impl  
  │   │       ├── FileReaderServiceImpl.java  # Implementation of file reading
  │   │       ├── TracingServiceImpl.java  # Implementation of tracing logic
  │   ├── DistributedTracing.java  # Main class
  ├── src/main/resources  
  │   ├── input.txt  # Required input file  
  ├── src/test/java/com/ibm/service  
  │   ├── TracingServiceImplTest.java  # Unit tests for tracing service
  ├── src/test/resources  
  │   ├── test_input.txt  # Required test input file  
  ├── README.md  
  ├── pom.xml 
```

## Dependencies

The project uses the following dependencies:

- SLF4J for logging
- JUnit 5 for testing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Building the Project

- Clone the repository to your local machine.
- Navigate to the project root directory.
- Run the following command to build the project:
   ```
   mvn clean install
   ```


### How to run the project for given program

- Place the required input file in the resource folder with name input.txt
- Execute the Main Class.

`com.ibm`: main class `DistributedTracing`.

- The output will be displayed in the console.

## Why These Approaches Were Used

### Key Design and Implementation Choices

1. **Centralized Graph Management:**
   - The `GraphRepository` is used to manage the graph data, ensuring reusability and separation of concerns. This keeps the graph structure consistent and centralized for all operations.

2. **Stream API for Efficiency:**
   - The `findAverageLatency` method leverages the Java Stream API to efficiently calculate the total latency between nodes in a trace.

3. **Recursive Path Computation:**
   - The method uses a recursive Depth-First Search (DFS) to explore all possible paths.

4. **Priority Queue for Shortest Paths:**
   - A `PriorityQueue` is used in the `findShortestTrace` method, implementing a Dijkstra-like algorithm to find the shortest path between nodes. This is optimal for weighted graph traversal.

5. **Thread-Safe Counters:**
   - The use of `AtomicInteger` ensures thread safety when counting paths in recursive methods, avoiding concurrency issues.

6. **Modular Design:**
   - Helper methods like `initializeDistances` and `checkNeighborsTraceNode` improve code reusability and clarity. 

7. **Error Validation:**
   - Methods like `isValidTrace` validate inputs upfront to prevent unnecessary computation or runtime errors, ensuring robustness.

## Future Enhancements

1. **Caching for Optimization:**
   - Introduce caching mechanisms to store results of frequently computed operations, such as shortest paths, to improve performance for large datasets.

2. **Dynamic Graph Updates:**
   - Add functionality to dynamically modify the graph by adding or removing nodes and edges.

3. **Parallel Processing:**
   - Utilize parallel streams or multi-threading to handle large-scale graphs more efficiently, particularly for path computations.

4. **Graph Visualization Tools:**
   - Integrate a graph visualization library to provide a graphical representation of the graph structure and trace paths.

5. **Support for Weighted Graph Variants:**
   - Extend the system to handle graphs with multiple edge attributes (e.g., cost and latency) to support more complex analyses.

6. **REST API Integration:**
   - Create a RESTful API to expose tracing services, making them accessible to external systems for integration.


Feel free to reach out if you have any questions or need assistance with further development!

## Developer Information
- Name: Asmita Tiwari
- Email: asmitatiwari185@gmail.com
