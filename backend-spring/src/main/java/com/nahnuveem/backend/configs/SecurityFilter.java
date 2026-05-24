package com.nahnuveem.backend.configs;

import com.nahnuveem.backend.models.Usuario;
import com.nahnuveem.backend.repositories.UsuarioRepository;
import com.nahnuveem.backend.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extrai o token do cabeçalho da requisição
        String token = recuperarToken(request);

        if (token != null) {
            // 2. Valida o token e recupera o email do usuário
            String email = tokenService.validarToken(token);

            if (!email.isEmpty()) {
                // 3. Busca o usuário no banco de dados
                Usuario usuario = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o e-mail do token."));

                // 4. Autentica o usuário no contexto do Spring Security
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Continua o fluxo normal da requisição (vai para o Controller)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para ler o header "Authorization" e remover o prefixo "Bearer "
    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}