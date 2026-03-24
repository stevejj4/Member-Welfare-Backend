package com.SUNData.MemberApp.Exceptions;

// For missing entities e.g. principal not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}