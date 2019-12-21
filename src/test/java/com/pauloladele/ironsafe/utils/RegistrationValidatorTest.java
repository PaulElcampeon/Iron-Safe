package com.pauloladele.ironsafe.utils;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.config.SecurityConfigurer;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.models.ValidationResponse;
import com.pauloladele.ironsafe.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = {IronSafeApplication.class, SecurityConfigurer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RegistrationValidatorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationValidator registrationValidator;

    private final String email = "test@live.com";

    private final String password = "testPassword";

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

        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password,  password);
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Email already exists", validationResponse.getMessage());
        verify(userService, times(1)).checkIfEmailExists(Mockito.anyString());

    }

    @Test
    public void validatePasswordsDoNotMatch() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password, password.concat("dontMatch"));
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Passwords do not match", validationResponse.getMessage());
    }

    @Test
    public void validateEmailsDoNotMatch() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email.concat("dontMatch"), password, password);
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Emails do not match", validationResponse.getMessage());
    }

    @Test
    public void validateEmailFormatIsNotCorrect() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest("t@livecom", "t@livecom", password, password);
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Email format is not correct", validationResponse.getMessage());
    }

    @Test
    public void validatePasswordIsInvalid() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, "2", "2");
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertFalse(validationResponse.isSuccess());
        assertEquals("Password is not valid", validationResponse.getMessage());
    }

    @Test
    public void validateCreated() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, email, password,  password);
        ValidationResponse validationResponse = registrationValidator.validate(createUserRequest);

        assertTrue(validationResponse.isSuccess());
        assertEquals("Created", validationResponse.getMessage());
    }
}
