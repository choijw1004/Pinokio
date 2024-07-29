package com.example.pinokkio.exception;

import java.util.HashMap;
import java.util.Map;

public class BaseException extends RuntimeException {
    private final String code;
    private Map<String, String> errors = new HashMap<>();

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Map<String, String> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }

    public String getCode() {
        return this.code;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }
}