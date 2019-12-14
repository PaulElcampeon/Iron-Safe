package com.pauloladele.ironsafe.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Data
@Document("USERS")
public class User {

    @Id
    private String email;
    private String password;
    private List<String> roles = Arrays.asList("USER");

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
