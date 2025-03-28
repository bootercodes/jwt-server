package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class.
 */
@SpringBootApplication
public class JwtServerApplication {

    /**
     * Entry point of the application
     *
     * @param args Main args
     */
    public static void main(String[] args) {
        // Spring boots entry point
        SpringApplication.run(JwtServerApplication.class, args);
    }
}
