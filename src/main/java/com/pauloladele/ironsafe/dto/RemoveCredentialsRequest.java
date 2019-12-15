package com.pauloladele.ironsafe.dto;

import lombok.Data;

@Data
public class RemoveCredentialsRequest {

    private String key;
    private String value;

    public RemoveCredentialsRequest() {
    }

    public RemoveCredentialsRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
