package com.example.controller;

import com.example.service.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userDetailsService1) {
        this.userDetailsService = userDetailsService1;
    }

    @GetMapping("/user")
    public String getUser() {
        final String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final UserDetails user = this.userDetailsService.loadUserByUsername(username);
        return String.format("'%s' is authorized to access this resource!", user.getUsername());
    }
}
