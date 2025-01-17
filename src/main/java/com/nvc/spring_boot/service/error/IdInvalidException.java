package com.nvc.spring_boot.service.error;

public class IdInvalidException extends Exception {
    public IdInvalidException(String message) {
        super(message);
    }
}
