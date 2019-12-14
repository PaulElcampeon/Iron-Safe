package com.pauloladele.ironsafe.services;

import com.pauloladele.ironsafe.models.CustomUserDetails;
import com.pauloladele.ironsafe.models.User;
import com.pauloladele.ironsafe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findById(email).orElseThrow(NoSuchElementException::new);
        return new CustomUserDetails(user);
    }
}
