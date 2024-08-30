package com.banca.fibonacci.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(name = "ErrorResponse", description = "Error response returned when an error occurs")
public class ErrorResponseDTO {

    @Schema(description = "HTTP status code", example = "400")
    private int statusCode;

    @Schema(description = "Timestamp of the error", example = "2024-08-30 10:22:02")
    private String timestamp;

    @Schema(description = "Error message", example = "El parámetro debe ser un número entero válido")
    private String message;

    @Schema(description = "Error description", example = "Failed to convert value of type 'java.lang.String'")
    private String description;
}
