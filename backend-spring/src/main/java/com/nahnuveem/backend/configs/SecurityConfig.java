package com.nahnuveem.backend.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter; // Injeta o filtro criado

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Rotas PÚBLICAS (Qualquer um acessa para criar conta ou fazer login)
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Rotas PROTEGIDAS (Só acessa quem estiver com token JWT válido)
                        .requestMatchers(HttpMethod.POST, "/produtos").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/produtos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/produtos/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/**").authenticated()

                        // Garante a proteção padrão para qualquer outra rota esquecida
                        .anyRequest().authenticated()
                )
                // Coloca o nosso filtro na esteira ANTES do filtro padrão de autenticação do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}