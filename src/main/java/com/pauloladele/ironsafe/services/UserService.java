package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.dto.AuthenticationRequest;
import com.pauloladele.ironsafe.dto.AuthenticationResponse;
import com.pauloladele.ironsafe.dto.CreateUserRequest;
import com.pauloladele.ironsafe.models.Safe;
import com.pauloladele.ironsafe.models.User;
import com.pauloladele.ironsafe.repositories.UserRepository;
import com.pauloladele.ironsafe.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserInterface {

    @Autowired
    private UserRepository repository;

    @Autowired
    private SafeService safeService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean createUser(CreateUserRequest createUserRequest) {
        repository.insert(new User(createUserRequest.getEmail(), passwordEncoder.encode(createUserRequest.getPassword())));

        safeService.createSafe(createUserRequest.getEmail());

        return true;
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        final Safe safe  = safeService.getSafe(authenticationRequest.getEmail());

        return new AuthenticationResponse(jwt, safe);
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return repository.existsById(email);
    }

}
