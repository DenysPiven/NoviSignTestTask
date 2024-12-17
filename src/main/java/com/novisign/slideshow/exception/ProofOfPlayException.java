package com.novisign.slideshow.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProofOfPlayException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public ProofOfPlayException(String message) {
        super(message);
        logger.error("ProofOfPlayException thrown: {}", message);
    }
}