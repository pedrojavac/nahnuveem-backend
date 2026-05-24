package com.nahnuveem.backend.services;

import com.nahnuveem.backend.dtos.UsuarioRequestDTO;
import com.nahnuveem.backend.dtos.UsuarioResponseDTO;
import com.nahnuveem.backend.models.Usuario;
import com.nahnuveem.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. CADASTRAR USUÁRIO (Com validação e senha criptografada)
    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO request) {
        // Validação de negócio: e-mail duplicado
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado no sistema");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        // CRIPTOGRAFIA: Transforma a senha pura (ex: "123") em um hash BCrypt seguro
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Salva a entidade no banco de dados com a senha protegida
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterParaResponseDTO(usuarioSalvo);
    }

    // 2. LISTAR TODOS OS USUÁRIOS
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    // MÉTODO AUXILIAR: Converte a Entidade para o DTO de Resposta
    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        return response;
    }
}