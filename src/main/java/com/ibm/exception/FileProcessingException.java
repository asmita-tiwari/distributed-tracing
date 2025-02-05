package com.ibm.exception;

/**
 * Custom exception class for handling file processing errors.
 */
public class FileProcessingException extends Exception {

    /**
     * Constructs a new FileProcessingException with the specified detail message.
     *
     * @param message The detail message.
     */
    public FileProcessingException(String message) {
        super(message);
    }
}
