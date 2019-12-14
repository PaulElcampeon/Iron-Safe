package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.repositories.SafeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SafeService implements SafeInterface {

    @Autowired
    private SafeRepository repository;

    @Override
    public void createSafe(String email) {
        repository.insert(new Safe(email));
    }

    @Override
    public Safe getSafe(String email) {
        return repository.findById(email).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public boolean addCredentials(AddCredentialsRequest addCredentialsMessage) {
        return false;
    }

    @Override
    public boolean removeCredentials(RemoveCredentialsRequest removeCredentialsMessage) {
        return false;
    }
}
