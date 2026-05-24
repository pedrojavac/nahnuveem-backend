package com.nahnuveem.backend.controllers;

import com.nahnuveem.backend.dtos.UsuarioRequestDTO;
import com.nahnuveem.backend.dtos.UsuarioResponseDTO;
import com.nahnuveem.backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioRepository;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody UsuarioRequestDTO request){
        UsuarioResponseDTO novoUsuario = usuarioRepository.cadastrarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos(){
        List<UsuarioResponseDTO> usuarios = usuarioRepository.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
}
