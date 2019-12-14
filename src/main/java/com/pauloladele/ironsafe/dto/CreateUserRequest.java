package com.pauloladele.ironsafe.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String email;
    private String confirmEmail;
    private String password;
    private String confirmPassword;
}
