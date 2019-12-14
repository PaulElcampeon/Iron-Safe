package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.dto.AuthenticationRequest;
import com.pauloladele.ironsafe.dto.AuthenticationResponse;

public interface UserInterface {

    boolean createUser(CreateUserRequest createUserMessage);

    AuthenticationResponse authenticateUser(AuthenticationRequest loginMessage) throws Exception;

    boolean checkIfEmailExists(String email);
}
