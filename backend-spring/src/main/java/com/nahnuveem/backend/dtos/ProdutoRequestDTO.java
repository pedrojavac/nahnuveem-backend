package com.nahnuveem.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome do produto é obrigatório e não pode ficar em branco.")
    @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres.")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço do produto é obrigatório.")
    @Positive(message = "O preço do produto deve ser maior que zero.")
    private BigDecimal preco;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    // Permite 0 ou números positivos, impedindo estoque negativo
    @jakarta.validation.constraints.Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private Integer quantidadeEstoque;

    @NotBlank(message = "O código de barras é obrigatório.")
    private String codigoBarras;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private UUID usuarioId;
}