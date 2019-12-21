package com.pauloladele.ironsafe.resources;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.dto.AuthenticationRequest;
import com.pauloladele.ironsafe.dto.AuthenticationResponse;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import com.pauloladele.ironsafe.repositories.UserRepository;
import com.pauloladele.ironsafe.services.UserService;
import com.pauloladele.ironsafe.utils.RegistrationValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = {IronSafeApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private SafeRepository safeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String email = "test@live.com";

    private final String password = "testPassword";

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        safeRepository.deleteAll();
    }

    @Test
    public void createUserResponseCreated() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/user/create", createUserRequest, String.class);

        assertEquals("Created", responseEntity.getBody());
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void createUserResponseBlankFields() {
        CreateUserRequest createUserRequest = new CreateUserRequest();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/user/create", createUserRequest, String.class);

        assertEquals("Cannot have blank fields", responseEntity.getBody());
        assertEquals(403, responseEntity.getStatusCodeValue());
    }

    @Test
    public void authenticateResponseSuccess() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password);

        userService.createUser(createUserRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity("/user/authenticate", authenticationRequest, AuthenticationResponse.class);

        assertNotNull(responseEntity.getBody().getJwt());
        assertEquals(202, responseEntity.getStatusCodeValue());
    }

    @Test
    public void authenticateResponseFailure() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity("/user/authenticate", authenticationRequest, AuthenticationResponse.class);

        assertEquals(403, responseEntity.getStatusCodeValue());
    }
}
