package com.pauloladele.ironsafe.utils;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.config.SecurityConfigurer;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.models.ValidationResponse;
import com.pauloladele.ironsafe.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IronSafeApplication.class, SecurityConfigurer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RegistrationValidatorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationValidator registrationValidator;

    @Test
    public void validateCheckEmptyFieldsTrue() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Cannot have blank fields", validationResponse.getMessage());
    }

    @Test
    public void validateEmailAlreadyExists() throws IOException {
        when(userService.checkIfEmailExists(Mockito.anyString())).thenReturn(true);

        CreateUserRequest createUserRequest = new CreateUserRequest("t@live.com", "t@live.com", "1234er", "1234er");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Email already exists", validationResponse.getMessage());
        verify(userService, times(1)).checkIfEmailExists(Mockito.anyString());

    }

    @Test
    public void validatePasswordsDoNotMatch() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest("t@live.com", "t@live.com", "1", "2");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Passwords do not match", validationResponse.getMessage());
    }

    @Test
    public void validateEmailsDoNotMatch() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest("t@live.com", "l@live.com", "2", "2");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Emails do not match", validationResponse.getMessage());
    }

    @Test
    public void validateEmailFormatIsNotCorrect() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest("t@livecom", "t@livecom", "2", "2");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Email format is not correct", validationResponse.getMessage());
    }

    @Test
    public void validatePasswordIsInvalid() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest("t@live.com", "t@live.com", "2", "2");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Password is not valid", validationResponse.getMessage());
    }

    @Test
    public void validateCreated() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest("t@live.com", "t@live.com", "12345", "12345");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertTrue(validationResponse.isSuccess());
        assertEquals("Created", validationResponse.getMessage());
    }
}
