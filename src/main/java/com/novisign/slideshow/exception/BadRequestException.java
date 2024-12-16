package com.novisign.slideshow.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadRequestException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(BadRequestException.class);

    public BadRequestException(String message) {
        super(message);
        logger.error("BadRequestException thrown: {}", message);
    }
}
