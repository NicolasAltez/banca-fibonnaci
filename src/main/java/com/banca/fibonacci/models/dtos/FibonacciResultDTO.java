package com.banca.fibonacci.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Schema(name = "FinonacciResult", description = "Fibonacci result returned when a fibonacci number is obtained")
public class FibonacciResultDTO {

    @Schema(description = "Position of the fibonacci number", example = "5")
    private int position;

    @Schema(description = " umber obtained", example = "5")
    private long value;

    @Schema(description = "Number of times the number has been requested", example = "20")
    private int queryCount;
}
