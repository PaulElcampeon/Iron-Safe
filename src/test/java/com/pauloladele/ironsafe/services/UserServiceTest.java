package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.config.SecurityConfigurer;
import com.pauloladele.ironsafe.dto.AuthenticationRequest;
import com.pauloladele.ironsafe.dto.AuthenticationResponse;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import com.pauloladele.ironsafe.repositories.UserRepository;
import com.pauloladele.ironsafe.utils.JwtUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IronSafeApplication.class, SecurityConfigurer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest {

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

    @After
    public void tearDown() {
        userRepository.deleteAll();
        safeRepository.deleteAll();
    }

    @Test
    public void createUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest("danny@live.com", "danny@live.com", "12345", "12345");

        userService.createUser(createUserRequest);

        final boolean exists = userRepository.existsById("danny@live.com");

        assertTrue(exists);
    }

    @Test
    public void authenticateUser() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("danny@live.com", "danny@live.com", "12345", "12345");

        userService.createUser(createUserRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("danny@live.com");
        authenticationRequest.setPassword("12345");

        final AuthenticationResponse authenticationResponse = userService.authenticateUser(authenticationRequest);

        assertNotNull(authenticationResponse.getJwt());
        assertNotNull(authenticationResponse.getSafe());
    }

    @Test(expected = InternalAuthenticationServiceException.class)
    public void authenticateUserException() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("danny@live.com");
        authenticationRequest.setPassword("12345");

        final AuthenticationResponse authenticationResponse = userService.authenticateUser(authenticationRequest);

        assertNotNull(authenticationResponse.getJwt());
        assertNotNull(authenticationResponse.getSafe());
    }

    @Test
    public void checkIfEmailExistsTrue() {
        CreateUserRequest createUserRequest = new CreateUserRequest("danny@live.com", "danny@live.com", "12345", "12345");

        userService.createUser(createUserRequest);

        final boolean exists = userService.checkIfEmailExists("danny@live.com");

        assertTrue(exists);
    }

    @Test
    public void checkIfEmailExistsFalse() {
        final boolean exists = userService.checkIfEmailExists("danny@live.com");

        assertFalse(exists);
    }
}
