package com.SUNData.MemberApp.Exceptions;

// For business rule violations e.g. registration for duplicate ID or phone number
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}