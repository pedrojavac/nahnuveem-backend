package com.nahnuveem.backend.controllers;

import com.nahnuveem.backend.dtos.ProdutoRequestDTO;
import com.nahnuveem.backend.dtos.ProdutoResponseDTO;
import com.nahnuveem.backend.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/produtos") // Rota base para gerenciamento do estoque
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Endpoint para cadastro (POST http://localhost:8080/produtos)
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(@Valid @RequestBody ProdutoRequestDTO request) {
        ProdutoResponseDTO response = produtoService.salvarProduto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint para listagem (GET http://localhost:8080/produtos)
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarTodos(
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {

        Page<ProdutoResponseDTO> paginaResponse = produtoService.listarTodos(paginacao);
        return ResponseEntity.ok(paginaResponse);
    }
    // Adicione estes métodos no seu ProdutoController.java

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable UUID id) {
        ProdutoResponseDTO response = produtoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody ProdutoRequestDTO request) {
        ProdutoResponseDTO response = produtoService.atualizarProduto(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        produtoService.deletarProduto(id);
        System.out.println("Deletado com sucesso !");
        return ResponseEntity.noContent().build(); // Retorna 204 No Content se deletado com sucesso
    }
 }