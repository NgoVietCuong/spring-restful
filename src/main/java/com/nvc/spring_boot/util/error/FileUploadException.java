package com.nvc.spring_boot.util.error;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }
}
