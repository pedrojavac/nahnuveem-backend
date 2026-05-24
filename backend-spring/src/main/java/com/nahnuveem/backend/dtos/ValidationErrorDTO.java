package com.nahnuveem.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorDTO {
    private String campo;
    private String mensagem;
}