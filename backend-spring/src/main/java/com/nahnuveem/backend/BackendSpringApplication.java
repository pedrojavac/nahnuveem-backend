package com.nahnuveem.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // Garanta este import

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) // Desativa o Spring Security localmente
public class BackendSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSpringApplication.class, args);
    }
}



