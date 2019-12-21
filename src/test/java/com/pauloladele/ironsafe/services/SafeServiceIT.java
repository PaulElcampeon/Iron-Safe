package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.config.SecurityConfigurer;
import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import com.pauloladele.ironsafe.utils.StringEncrypterDecrypter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = {IronSafeApplication.class, SecurityConfigurer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SafeServiceIT {

    @Autowired
    private SafeRepository repository;

    @Autowired
    private SafeService safeService;

    @Autowired
    private StringEncrypterDecrypter encrypterDecrypter;

    private final String email = "test@live.com";

    private final String key = "testKey";

    private final String value = "testValue";

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void createSafeSuccess() {
        safeService.createSafe(email);

        assertTrue(repository.existsById(email));
    }

    @Test
    public void getSafeSuccess() {
        safeService.createSafe(email);

        final Safe safe = safeService.getSafe(email);

        assertEquals(email, safe.getEmail());
    }

    @Test
    public void addCredentialsSuccess() {
        safeService.createSafe(email);

        final AddCredentialsRequest addFirstCredentialsRequest = new AddCredentialsRequest(key, value);
        final AddCredentialsRequest addSecondCredentialsRequest = new AddCredentialsRequest(key.concat("2"), value.concat("2"));

        boolean success1 = safeService.addCredentials(addFirstCredentialsRequest, email);
        boolean success2 = safeService.addCredentials(addSecondCredentialsRequest, email);

        final Safe safe = safeService.getSafe(email);

        assertTrue(success1);
        assertTrue(success2);
        assertEquals(2, safe.getCredentials().size());
        assertEquals(addFirstCredentialsRequest.getKey() + "." + addFirstCredentialsRequest.getValue(), safe.getCredentials().get(0));
        assertEquals(addSecondCredentialsRequest.getKey() + "." + addSecondCredentialsRequest.getValue(), safe.getCredentials().get(1));
    }

    @Test
    public void removeCredentialsSuccess() {
        safeService.createSafe(email);

        final AddCredentialsRequest firstCredential = new AddCredentialsRequest(key, value);

        final AddCredentialsRequest secondCredential = new AddCredentialsRequest("Test2", "TestPassword2");

        safeService.addCredentials(firstCredential, email);
        safeService.addCredentials(secondCredential, email);

        final RemoveCredentialsRequest removeFirstCredentialRequest = new RemoveCredentialsRequest(key, value);

        final boolean success = safeService.removeCredentials(removeFirstCredentialRequest, email);

        final Safe safe = safeService.getSafe(email);

        assertTrue(success);
        assertEquals(1, safe.getCredentials().size());
        assertEquals(secondCredential.getKey() + "." + secondCredential.getValue(), safe.getCredentials().get(0));
    }
}
