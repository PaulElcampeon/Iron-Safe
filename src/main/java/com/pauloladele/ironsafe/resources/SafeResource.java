package com.pauloladele.ironsafe.resources;

import com.pauloladele.ironsafe.dto.AddCredentialsRequest;
import com.pauloladele.ironsafe.dto.RemoveCredentialsRequest;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.services.SafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("safe")
public class SafeResource {

    @Autowired
    private SafeService safeService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> getSafe(Principal principal) {

        final Safe safe = safeService.getSafe(principal.getName());

        return new ResponseEntity<>(safe, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/add/credential", method = RequestMethod.POST)
    public ResponseEntity<?> addCredential(@RequestBody AddCredentialsRequest addCredentialsRequest, Principal principal) {

        final boolean success = safeService.addCredentials(addCredentialsRequest, principal.getName());

        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @RequestMapping(value = "/remove/credential", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeCredential(@RequestBody RemoveCredentialsRequest removeCredentialsRequest, Principal principal) {

        final boolean success  = safeService.removeCredentials(removeCredentialsRequest, principal.getName());

        return new ResponseEntity<>(success, HttpStatus.ACCEPTED);
    }
}
