package com.example.config;

import com.example.util.JwtTokenFilter;
import com.example.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.util.SecurityRoles.ADMIN;
import static com.example.util.SecurityRoles.USER;

/**
 * Spring security config to define SecurityFilterChain for rules on how to handle the incoming HTTP request to the server.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider1) {
        this.jwtTokenProvider = jwtTokenProvider1;
    }

    /**
     * Bean defines how to process the incoming HTTP requests to the server.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // disabled csrf
                .authorizeHttpRequests(authMgrReqMatcherReg ->
                        authMgrReqMatcherReg.requestMatchers("auth/login").permitAll() // Don't run any auth logic since this is entry point to get JWT
                                .requestMatchers("api/**").hasAnyRole(ADMIN.name(), USER.name()) // Must have USER role
                                .requestMatchers("admin/**").hasRole(ADMIN.name()) // Must have ADMIN role
                                .anyRequest().authenticated())
                //.exceptionHandling() // TODO -- Exception handling can also be updated to return more personalized error response ( defaults to 403 Forbidden )
                // Add Custom Jwt filter which replaces the default user/password filter provided by spring-security
                .addFilterAt(new JwtTokenFilter(this.jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Bean to generate or compare the hashed passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
