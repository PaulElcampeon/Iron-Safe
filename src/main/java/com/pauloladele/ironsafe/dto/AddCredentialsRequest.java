package com.pauloladele.ironsafe.dto;

import lombok.Data;

@Data
public class AddCredentialsRequest {

    private String key;
    private String value;

    public AddCredentialsRequest() {
    }

    public AddCredentialsRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
