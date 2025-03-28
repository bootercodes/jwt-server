package com.example.controller;

import com.example.service.CustomUserDetailsService;
import com.example.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Auth rest controller to authenticated user and to retrieve JWT token for authorization to other server resources.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Logger
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Dependencies
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor.
     */
    public AuthController(JwtTokenProvider jwtProvider, PasswordEncoder passwordEncoder,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = customUserDetailsService;
    }

    /**
     * Entry point for a user to get authenticated.
     *
     * @param loginData login data from the body
     * @return JWT token for this authenticated user. Otherwise, empty if user was not found.
     */
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // If user is found, then compare the submitted password
        try {
            if (this.passwordEncoder.matches(password, this.userDetailsService.loadUserByUsername(username).getPassword())) {
                return this.jwtTokenProvider.generateToken(this.userDetailsService.loadUserByUsername(username));
            }
        } catch (UsernameNotFoundException | IllegalArgumentException ex) {
            log.error(ex.getMessage());
        }
        return "Invalid credentials";
    }
}
