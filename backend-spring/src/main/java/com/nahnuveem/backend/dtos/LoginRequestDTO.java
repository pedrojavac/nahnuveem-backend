package com.nahnuveem.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail enviado deve ser válido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;
}