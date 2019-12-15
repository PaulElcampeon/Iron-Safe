package com.pauloladele.ironsafe.utils;

import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.models.ValidationResponse;
import com.pauloladele.ironsafe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class RegistrationValidator {

    private UserService userService;

    @Autowired
    public RegistrationValidator(UserService userService) {
        this.userService = userService;
    }

    public ValidationResponse validate(CreateUserRequest createUserRequest) throws IOException {
        if (checkEmptyFields(createUserRequest)) {
            return new ValidationResponse(false, "Cannot have blank fields");
        } else if (checkIfUEmailExists(createUserRequest.getEmail())) {
            return new ValidationResponse(false, "Email already exists");
        } else if (checkIfFieldsAreNotEqual(createUserRequest.getPassword(), createUserRequest.getConfirmPassword())) {
            return new ValidationResponse(false, "Passwords do not match");
        } else if (checkIfFieldsAreNotEqual(createUserRequest.getEmail(), createUserRequest.getConfirmEmail())) {
            return new ValidationResponse(false, "Emails do not match");
        } else if (checkIfEmailDoesNotIncludesKeyCharacters(createUserRequest.getEmail())) {
            return new ValidationResponse(false, "Email format is not correct");
        } else if (checkPasswordIsInValid(createUserRequest.getPassword())) {
            return new ValidationResponse(false, "Password is not valid");
        } else {
            return new ValidationResponse(true, "Created");
        }
    }

    private boolean checkEmptyFields(CreateUserRequest createUserRequest) {
        return (
                createUserRequest.getPassword() == null ||
                        createUserRequest.getConfirmPassword()  == null ||
                        createUserRequest.getEmail() == null||
                        createUserRequest.getConfirmEmail()  == null
        );
    }

    private boolean checkIfUEmailExists(String email) {
        return userService.checkIfEmailExists(email);
    }

    private boolean checkIfEmailDoesNotIncludesKeyCharacters(String email) {
        return !email.contains("@") || (StringUtils.countOccurrencesOf(email, ".") == 0);
    }

    private boolean checkIfFieldsAreNotEqual(String field1, String field2) {
        return !field1.equals(field2);
    }

    private boolean checkPasswordIsInValid(String password) {
        return password.length() < 5;
    }
}
