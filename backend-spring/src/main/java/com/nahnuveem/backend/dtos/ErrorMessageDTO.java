package com.nahnuveem.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorMessageDTO {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}