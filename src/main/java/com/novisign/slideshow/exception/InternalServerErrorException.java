package com.novisign.slideshow.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalServerErrorException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(InternalServerErrorException.class);

    public InternalServerErrorException(String message) {
        super(message);
        logger.error("InternalServerErrorException thrown: {}", message);
    }
}
