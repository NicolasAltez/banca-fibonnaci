package com.banca.fibonacci.service;

import com.banca.fibonacci.controller.advice.GenericControllerAdvice;
import com.banca.fibonacci.models.dtos.FibonacciResultDTO;
import com.banca.fibonacci.models.entity.FibonacciResult;
import com.banca.fibonacci.repository.FibonacciResultRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FibonacciResultService {

    private final FibonacciResultRepository fibonacciResultRepository;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(FibonacciResultService.class);

    public FibonacciResultService(FibonacciResultRepository fibonacciResultRepository, ModelMapper modelMapper) {
        this.fibonacciResultRepository = fibonacciResultRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public long getFibonacci(int n) {
        LOGGER.info("Calculating fibonacci for position {}", n);

        return fibonacciResultRepository.findByPosition(n)
                .map(this::updateAndReturnExistingFibonacci)
                .orElseGet(() -> computeAndSaveFibonacci(n));
    }

    private long updateAndReturnExistingFibonacci(FibonacciResult existingResult) {
        LOGGER.info("Updating existing Fibonacci result for position: {}", existingResult.getPosition());

        existingResult.setQueryCount(existingResult.getQueryCount() + 1);
        fibonacciResultRepository.save(existingResult);

        LOGGER.info("Updated existing Fibonacci result for position: {}", existingResult.getPosition());

        return existingResult.getValue();
    }

    private long computeAndSaveFibonacci(int n) {
        LOGGER.info("Computing and saving Fibonacci result for position: {}", n);

        long result = calculateFibonacci(n);
        saveFibonacciResult(n, result);

        LOGGER.info("Computed and saved Fibonacci result for position: {}", n);

        return result;
    }

    private void saveFibonacciResult(int n, long result) {
        fibonacciResultRepository.save(FibonacciResult.builder()
                .position(n)
                .queryCount(1)
                .value(result)
                .build());
    }

    private long calculateFibonacci(int n) {
        if (isBaseCase(n)) return n;
        return computeFibonacci(n);
    }

    private boolean isBaseCase(int n) {
        return n == 0 || n == 1;
    }

    private long computeFibonacci(int n) {
        long a = 0;
        long b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = a;
            a = b;
            b += temp;
        }
        return b;
    }


    public List<FibonacciResultDTO> getLeastRequestedResultsTop10() {
        return fibonacciResultRepository.findTop10ByOrderByQueryCountAsc()
                .stream()
                .map(this::convertToDto).toList();
    }

    public List<FibonacciResultDTO> getMostRequestedResultsTop10() {
        return fibonacciResultRepository.findTop10ByOrderByQueryCountDesc()
                .stream()
                .map(this::convertToDto).toList();
    }

    private FibonacciResultDTO convertToDto(FibonacciResult result) {
        return modelMapper.map(result, FibonacciResultDTO.class);
    }
}
