package com.nvc.spring_boot.util.error;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
