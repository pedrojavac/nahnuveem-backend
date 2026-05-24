package com.nahnuveem.backend.controllers;

import com.nahnuveem.backend.dtos.LoginRequestDTO;
import com.nahnuveem.backend.dtos.LoginResponseDTO;
import com.nahnuveem.backend.models.Usuario;
import com.nahnuveem.backend.repositories.UsuarioRepository;
import com.nahnuveem.backend.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // 1. Verifica se o usuário existe pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos."));

        // 2. Compara a senha digitada com a senha criptografada do banco
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        // 3. Se tudo estiver correto, gera o token JWT
        String token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}