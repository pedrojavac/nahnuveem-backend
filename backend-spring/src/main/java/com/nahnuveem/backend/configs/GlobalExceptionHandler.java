package com.nahnuveem.backend.configs;

import com.nahnuveem.backend.dtos.ErrorMessageDTO;
import com.nahnuveem.backend.dtos.ValidationErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Captura as exceções manuais lançadas no seu Service (RuntimeExceptions)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageDTO> handleRuntimeException(RuntimeException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorMessageDTO errorResponse = new ErrorMessageDTO(
                Instant.now(),
                status.value(),
                "Erro na validação do negócio",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    // 2. Captura AUTOMATICAMENTE as falhas das anotações @Valid (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Mapeia todos os erros de campos da exceção e armazena as mensagens amigáveis
        List<ValidationErrorDTO> errosDeValidacao = new ArrayList<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errosDeValidacao.add(new ValidationErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        // Transforma a lista de erros em uma String amigável para o DTO principal
        String mensagemConsolidada = errosDeValidacao.stream()
                .map(err -> err.getCampo() + ": " + err.getMensagem())
                .reduce((a, b) -> a + " | " + b)
                .orElse("Erro de validação nos campos.");

        ErrorMessageDTO errorResponse = new ErrorMessageDTO(
                Instant.now(),
                status.value(),
                "Erro de validação nos campos enviados",
                mensagemConsolidada,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}