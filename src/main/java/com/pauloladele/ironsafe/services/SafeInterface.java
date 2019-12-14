package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Safe;

public interface SafeInterface {

    void createSafe(String email);

    Safe getSafe(String email);

    boolean addCredentials(AddCredentialsRequest addCredentialsMessage);

    boolean removeCredentials(RemoveCredentialsRequest removeCredentialsMessage);
}
