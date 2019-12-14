package com.pauloladele.ironsafe.resources;

import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.services.SafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
