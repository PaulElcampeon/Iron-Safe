package com.pauloladele.ironsafe.models;

import lombok.Data;

@Data
public class Credential {

    private String key;
    private String value;

    public Credential() {
    }

    public Credential(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
