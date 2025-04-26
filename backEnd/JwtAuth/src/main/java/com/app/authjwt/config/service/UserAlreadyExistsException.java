package com.app.authjwt.config.service;

import org.hibernate.service.spi.ServiceException;

public class UserAlreadyExistsException extends ServiceException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}


