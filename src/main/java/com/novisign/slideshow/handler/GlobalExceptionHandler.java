package com.novisign.slideshow.handler;

import com.novisign.slideshow.exception.BadRequestException;
import com.novisign.slideshow.exception.InternalServerErrorException;
import com.novisign.slideshow.exception.ProofOfPlayException;
import com.novisign.slideshow.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        logger.error("BadRequestException occurred: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException occurred: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "Resource Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Map<String, String>> handleInternalError(InternalServerErrorException ex) {
        logger.error("InternalServerErrorException occurred: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProofOfPlayException.class)
    public ResponseEntity<Map<String, String>> handleProofOfPlayException(ProofOfPlayException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Proof of Play Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
