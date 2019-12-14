package com.pauloladele.ironsafe.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("SAFES")
public class Safe {

    @Id
    private String email;
    private List<Credential> credentials = new ArrayList<>();

    public Safe() {}

    public Safe(String email) {
        this.email = email;
    }
}
