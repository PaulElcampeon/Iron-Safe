package com.pauloladele.ironsafe.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String email;
    private String confirmEmail;
    private String password;
    private String confirmPassword;

    public CreateUserRequest() {}

    public CreateUserRequest(String email, String confirmEmail, String password, String confirmPassword) {
        this.email = email;
        this.confirmEmail = confirmEmail;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
