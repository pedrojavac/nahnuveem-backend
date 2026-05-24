package com.nahnuveem.backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nahnuveem.backend.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Lê a chave secreta definida no application.properties, ou usa uma padrão
    @Value("${api.security.token.secret:minha-chave-secreta-super-protegida}")
    private String secret;

    // Gera o Token JWT para o usuário autenticado
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getEmail()) // Identifica o usuário pelo e-mail
                    .withClaim("id", usuario.getId().toString()) // Guarda também o UUID
                    .withExpiresAt(gerarDataExpiracao()) // Expira em 2 horas
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token de autenticação", exception);
        }
    }

    // Valida o Token recebido e extrai o e-mail do usuário
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return ""; // Retorna string vazia se o token estiver corrompido ou expirado
        }
    }

    // Define o tempo de expiração do token (padrão UTC-3 de Brasília)
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}