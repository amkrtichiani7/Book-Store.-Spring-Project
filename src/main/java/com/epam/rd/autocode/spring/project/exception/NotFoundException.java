package com.epam.rd.autocode.spring.project.exception;

public class NotFoundException extends BookstoreException {
    public NotFoundException(String message) {
        super(message);
    }
}