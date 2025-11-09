package com.aftourism.common.exception;

/**
 * Business level exception to surface domain errors to REST layer.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
