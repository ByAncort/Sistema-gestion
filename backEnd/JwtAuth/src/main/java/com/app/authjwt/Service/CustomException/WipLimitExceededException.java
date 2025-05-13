package com.app.authjwt.Service.CustomException;

public class WipLimitExceededException extends RuntimeException {
    public WipLimitExceededException(String message) {
        super(message);
    }
}