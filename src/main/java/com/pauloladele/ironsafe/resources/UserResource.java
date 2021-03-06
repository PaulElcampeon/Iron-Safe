package com.pauloladele.ironsafe.resources;

import com.pauloladele.ironsafe.dto.AuthenticationRequest;
import com.pauloladele.ironsafe.dto.AuthenticationResponse;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.models.ValidationResponse;
import com.pauloladele.ironsafe.services.UserService;
import com.pauloladele.ironsafe.utils.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationValidator registrationValidator;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserMessage) {
        ValidationResponse validationResponse;
        try {
            validationResponse = registrationValidator.validate(createUserMessage);
            if (validationResponse.isSuccess()) {
                userService.createUser(createUserMessage);
                return new ResponseEntity<>(validationResponse.getMessage(), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(validationResponse.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        AuthenticationResponse resp = userService.authenticateUser(authenticationRequest);

        return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
    }
}
