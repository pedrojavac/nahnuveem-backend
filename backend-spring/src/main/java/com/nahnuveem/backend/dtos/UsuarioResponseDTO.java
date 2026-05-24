package com.nahnuveem.backend.dtos;


import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioResponseDTO {
    private UUID id;
    private String nome;
    private String email;
}
