package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.config.SecurityConfigurer;
import com.pauloladele.ironsafe.dto.AuthenticationRequest;
import com.pauloladele.ironsafe.dto.AuthenticationResponse;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import com.pauloladele.ironsafe.repositories.UserRepository;
import com.pauloladele.ironsafe.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = {IronSafeApplication.class, SecurityConfigurer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SafeRepository safeRepository;

    @Autowired
    private SafeService safeService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private final String email = "test@live.com";

    private final String password = "testPassword";

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        safeRepository.deleteAll();
    }

    @Test
    public void createUserSuccess() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password);

        userService.createUser(createUserRequest);

        final boolean exists = userRepository.existsById(email);

        assertTrue(exists);
    }

    @Test
    public void authenticateUserSuccess() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password);

        userService.createUser(createUserRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        final AuthenticationResponse authenticationResponse;

        try {
            authenticationResponse = userService.authenticateUser(authenticationRequest);
            assertNotNull(authenticationResponse.getJwt());
            assertNotNull(authenticationResponse.getSafe());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void authenticateUserException() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        assertThrows(InternalAuthenticationServiceException.class, () ->
                userService.authenticateUser(authenticationRequest)
        );
    }

    @Test
    public void checkIfEmailExistsTrue() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password);

        userService.createUser(createUserRequest);

        final boolean exists = userService.checkIfEmailExists(email);

        assertTrue(exists);
    }

    @Test
    public void checkIfEmailExistsFalse() {
        final boolean exists = userService.checkIfEmailExists(email);

        assertFalse(exists);
    }
}
