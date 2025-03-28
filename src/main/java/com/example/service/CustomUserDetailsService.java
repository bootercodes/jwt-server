package com.example.service;

import com.example.util.SecurityRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to return user info.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    // TODO -- Temporary -- Replace with DB as a User info store
    private final List<UserDetails> users = new ArrayList<>();

    public CustomUserDetailsService(PasswordEncoder passwordEncoder1) {
        final String superSecretPassword = passwordEncoder1.encode("password"); // Store encoded passwords

        // TODO -- Hardcoded users
        users.add(User.withUsername("Employee")
                .password(superSecretPassword)
                .roles(SecurityRoles.USER.name())
                .build());
        users.add(User.withUsername("Manager")
                .password(superSecretPassword)
                .roles(SecurityRoles.ADMIN.name())
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found - " + username));
    }
}
