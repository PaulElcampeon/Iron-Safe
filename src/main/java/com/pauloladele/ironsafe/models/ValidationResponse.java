package com.pauloladele.ironsafe.models;

import lombok.Data;

@Data
public class ValidationResponse {

    private boolean success;
    private String message;

    public ValidationResponse() {}

    public ValidationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
