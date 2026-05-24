package com.nahnuveem.backend.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProdutoResponseDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private String codigoBarras;
    private UUID usuarioId; // Importante para expor o ID do usuário dono
}