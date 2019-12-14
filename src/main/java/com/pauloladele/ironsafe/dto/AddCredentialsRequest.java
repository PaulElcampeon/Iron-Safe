package com.pauloladele.ironsafe.dto;

import lombok.Data;

@Data
public class AddCredentialsRequest {

    private String key;
    private String value;
}
