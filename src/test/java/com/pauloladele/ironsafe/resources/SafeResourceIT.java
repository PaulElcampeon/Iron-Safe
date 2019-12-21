package com.pauloladele.ironsafe.resources;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.CustomUserDetails;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.models.User;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import com.pauloladele.ironsafe.repositories.UserRepository;
import com.pauloladele.ironsafe.services.SafeService;
import com.pauloladele.ironsafe.services.UserService;
import com.pauloladele.ironsafe.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = {IronSafeApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SafeResourceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SafeRepository safeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SafeService safeService;

    @Autowired
    private JwtUtil jwtUtil;

    private String jwt;

    private final String email = "test@live.com";

    private final String password = "testPassword";

    private final String key = "testKey";

    private final String value = "testValue";

    @BeforeEach
    public void init() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password);

        userService.createUser(createUserRequest);

        User user = userRepository.findById(email).get();

        jwt = jwtUtil.generateToken(new CustomUserDetails(user));
    }

    @AfterEach
    public void teardown() {
        userRepository.deleteAll();
        safeRepository.deleteAll();
    }

    @Test
    public void getSafeSuccess() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + jwt);

        ResponseEntity<Safe> responseEntity = restTemplate.exchange("/safe/get", HttpMethod.GET, new HttpEntity<>(headers), Safe.class);

        assertNotNull(responseEntity.getBody().getEmail());
        assertEquals(202, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getSafeAccessDenied() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        ResponseEntity<Safe> responseEntity = restTemplate.exchange("/safe/get", HttpMethod.GET, new HttpEntity<>(headers), Safe.class);

        assertEquals(403, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addCredentialSuccess() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer "+jwt);

        AddCredentialsRequest addCredentialsRequest = new AddCredentialsRequest(key, value);

        HttpEntity<AddCredentialsRequest> httpEntity = new HttpEntity<>(addCredentialsRequest, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/safe/add/credential", HttpMethod.POST, httpEntity, Boolean.class);

        assertTrue(responseEntity.getBody().booleanValue());
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addCredentialAccessDenied() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        AddCredentialsRequest addCredentialsRequest = new AddCredentialsRequest(key, value);

        HttpEntity<AddCredentialsRequest> httpEntity = new HttpEntity<>(addCredentialsRequest, headers);

        ResponseEntity<Exception> responseEntity = restTemplate.exchange("/safe/add/credential", HttpMethod.POST, httpEntity, Exception.class);

        assertEquals("Access Denied", responseEntity.getBody().getMessage());
        assertEquals(403, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCredentialSuccess() {
        AddCredentialsRequest addCredentialsRequest = new AddCredentialsRequest(key, value);

        safeService.addCredentials(addCredentialsRequest, email);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer "+jwt);

        RemoveCredentialsRequest removeCredentialsRequest = new RemoveCredentialsRequest(key, value);

        HttpEntity<RemoveCredentialsRequest> httpEntity = new HttpEntity<>(removeCredentialsRequest, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/safe/remove/credential", HttpMethod.DELETE, httpEntity, Boolean.class);

        assertTrue(responseEntity.getBody().booleanValue());
        assertEquals(202, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCredentialFalseNoCredentialsFound() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer "+jwt);

        RemoveCredentialsRequest removeCredentialsRequest = new RemoveCredentialsRequest(key, value);

        HttpEntity<RemoveCredentialsRequest> httpEntity = new HttpEntity<>(removeCredentialsRequest, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/safe/remove/credential", HttpMethod.DELETE, httpEntity, Boolean.class);

        assertFalse(responseEntity.getBody().booleanValue());
        assertEquals(202, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCredentialAccessDenied() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        RemoveCredentialsRequest removeCredentialsRequest = new RemoveCredentialsRequest(key, value);

        HttpEntity<RemoveCredentialsRequest> httpEntity = new HttpEntity<>(removeCredentialsRequest, headers);

        ResponseEntity<Exception> responseEntity = restTemplate.exchange("/safe/remove/credential", HttpMethod.DELETE, httpEntity, Exception.class);

        assertEquals("Access Denied", responseEntity.getBody().getMessage());
        assertEquals(403, responseEntity.getStatusCodeValue());
    }
}
