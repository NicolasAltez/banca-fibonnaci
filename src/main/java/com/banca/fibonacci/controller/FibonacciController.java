package com.banca.fibonacci.controller;


import com.banca.fibonacci.models.dtos.ErrorResponseDTO;
import com.banca.fibonacci.models.dtos.FibonacciResultDTO;
import com.banca.fibonacci.service.FibonacciResultService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fibonacci")
@Validated
public class FibonacciController {

    private final FibonacciResultService fibonacciService;
    public FibonacciController(FibonacciResultService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @Operation(summary = "Obtain the fibonacci number at a given position", description = "Obtain the fibonacci number at a given position")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fibonacci number obtained correctly",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/{position}")
    @RateLimiter(name = "fibonacci")
    public ResponseEntity<Long> getFibonacci(
            @PathVariable
            @Min(value = 0, message = "La posición no debe ser negativa")
            Integer position)  {
        long result = fibonacciService.getFibonacci(position);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "obtain least requested fibonacci numbers", description = "obtain least requested fibonacci numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Least requested fibonacci numbers obtained correctly",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FibonacciResultDTO.class)))}),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/least-requested/top10")
    @RateLimiter(name = "fibonacci")
    public ResponseEntity<List<FibonacciResultDTO>> getLeastRequestedResultsTop10() {
        List<FibonacciResultDTO> leastRequestedResults = fibonacciService.getLeastRequestedResultsTop10();
        return ResponseEntity.ok(leastRequestedResults);
    }


    @Operation(summary = "obtain most requested fibonacci numbers", description = "obtain most requested fibonacci numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Most fibonacci numbers obtained correctly",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FibonacciResultDTO.class)))}),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/most-requested/top10")
    @RateLimiter(name = "fibonacci")
    public ResponseEntity<List<FibonacciResultDTO>> getMostRequestedResultsTop10() {
        List<FibonacciResultDTO> mostRequestedResults = fibonacciService.getMostRequestedResultsTop10();
        return ResponseEntity.ok(mostRequestedResults);
    }
}
