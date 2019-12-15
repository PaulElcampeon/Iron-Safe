package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.IronSafeApplication;
import com.pauloladele.ironsafe.config.SecurityConfigurer;
import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IronSafeApplication.class, SecurityConfigurer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SafeServiceIT {

    @Autowired
    private SafeRepository repository;

    @Autowired
    private SafeService safeService;

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void createSafe() {
        final String email = "danny@live.com";

        safeService.createSafe(email);

        assertTrue(repository.existsById(email));
    }

    @Test
    public void getSafe() {
        final String email = "danny@live.com";

        safeService.createSafe(email);

        final Safe safe = safeService.getSafe(email);

        assertEquals(email, safe.getEmail());
    }

    @Test
    public void addCredentials() {
        final String email = "danny@live.com";

        safeService.createSafe(email);

        AddCredentialsRequest addCredentialsRequest = new AddCredentialsRequest();
        addCredentialsRequest.setKey("Llyods TSB");
        addCredentialsRequest.setValue("elca1");

        boolean success = safeService.addCredentials(addCredentialsRequest, email);

        final Safe safe = safeService.getSafe(email);

        assertTrue(success);
        assertEquals("Llyods TSB", safe.getCredentials().get(0).getKey());
    }

    @Test
    public void removeCredentials() {
        final String email = "danny@live.com";

        safeService.createSafe(email);

        final AddCredentialsRequest addCredentialsRequest1 = new AddCredentialsRequest();
        addCredentialsRequest1.setKey("Llyods1 TSB");
        addCredentialsRequest1.setValue("elca1");

        final AddCredentialsRequest addCredentialsRequest2 = new AddCredentialsRequest();
        addCredentialsRequest2.setKey("Llyods2 TSB");
        addCredentialsRequest2.setValue("elca1");

        safeService.addCredentials(addCredentialsRequest1, email);
        safeService.addCredentials(addCredentialsRequest2, email);

        final RemoveCredentialsRequest removeCredentialsRequest = new RemoveCredentialsRequest("Llyods1 TSB", "elca1");

        final boolean success  = safeService.removeCredentials(removeCredentialsRequest, email);

        final Safe safe = safeService.getSafe(email);

        assertTrue(success);
        assertEquals(1, safe.getCredentials().size());
    }
}
