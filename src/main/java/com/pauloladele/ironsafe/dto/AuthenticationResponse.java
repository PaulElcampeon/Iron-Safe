package com.pauloladele.ironsafe.dto;

import com.pauloladele.ironsafe.models.Safe;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;
    private Safe safe;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String jwt, Safe safe) {
        this.jwt = jwt;
        this.safe = safe;
    }
}
