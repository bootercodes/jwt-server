package com.example.controller;

import com.example.service.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final CustomUserDetailsService userDetailsService;

    public AdminController(CustomUserDetailsService userDetailsService1) {
        this.userDetailsService = userDetailsService1;
    }

    @GetMapping
    public String getAdmin() {
        final String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final UserDetails user = this.userDetailsService.loadUserByUsername(username);
        return String.format("'%s' is authorized to access this resource!", user.getUsername());
    }
}
