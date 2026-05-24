package com.nahnuveem.backend.dtos;

import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String nome;
    private String email;
    private String senha;
}
