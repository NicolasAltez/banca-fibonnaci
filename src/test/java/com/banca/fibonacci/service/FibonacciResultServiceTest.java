package com.banca.fibonacci.service;


import com.banca.fibonacci.models.dtos.FibonacciResultDTO;
import com.banca.fibonacci.models.entity.FibonacciResult;
import com.banca.fibonacci.repository.FibonacciResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FibonacciResultServiceTest {
    @Mock
    private FibonacciResultRepository fibonacciResultRepository;

    @InjectMocks
    private FibonacciResultService fibonacciResultService;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class getFibonnaciTests{
        @Test
        void testGetFibonacci_ExistingResult() {
            FibonacciResult existingResult = new FibonacciResult(1L, 5, 5L, 1);
            when(fibonacciResultRepository.findByPosition(5)).thenReturn(Optional.of(existingResult));

            long result = fibonacciResultService.getFibonacci(5);

            assertEquals(5L, result);

            verify(fibonacciResultRepository).findByPosition(5);
            verify(fibonacciResultRepository).save(existingResult);

            assertEquals(2, existingResult.getQueryCount());
        }

        @Test
        void testGetFibonacci_NewResult() {
            when(fibonacciResultRepository.findByPosition(7)).thenReturn(Optional.empty());

            long result = fibonacciResultService.getFibonacci(7);

            assertEquals(13L, result);

            verify(fibonacciResultRepository).findByPosition(7);
            verify(fibonacciResultRepository).save(any(FibonacciResult.class));
        }
    }
    @Nested
    class getLeastRequestdResultsTop10Tests{
        @Test
        void testGetLeastRequestedResultsTop10_Limit() {
            List<FibonacciResult> exactly10Results = IntStream.range(0, 10)
                    .mapToObj(i -> new FibonacciResult((long) i, i, (long) i, i))
                    .toList();

            when(fibonacciResultRepository.findTop10ByOrderByQueryCountAsc()).thenReturn(exactly10Results);

            List<FibonacciResultDTO> results = fibonacciResultService.getLeastRequestedResultsTop10();

            assertEquals(10, results.size());
        }


        @Test
        void testGetLeastRequestedResultsTop10_NoResults() {
            when(fibonacciResultRepository.findTop10ByOrderByQueryCountAsc()).thenReturn(Collections.emptyList());

            List<FibonacciResultDTO> results = fibonacciResultService.getLeastRequestedResultsTop10();

            assertTrue(results.isEmpty());
            verify(fibonacciResultRepository).findTop10ByOrderByQueryCountAsc();
        }
    }


    @Nested
    class getMostRequestedResultsTop10Tests{
        @Test
        void testGetMostRequestedResultsTop10_Limit() {
            List<FibonacciResult> exactly10Results = IntStream.range(0, 10)
                    .mapToObj(i -> new FibonacciResult((long) i, i, (long) i, i))
                    .toList();

            when(fibonacciResultRepository.findTop10ByOrderByQueryCountDesc()).thenReturn(exactly10Results);

            List<FibonacciResultDTO> results = fibonacciResultService.getMostRequestedResultsTop10();

            assertEquals(10, results.size());
        }

        @Test
        void testGetMostRequestedResultsTop10_NoResults() {
            when(fibonacciResultRepository.findTop10ByOrderByQueryCountDesc()).thenReturn(Collections.emptyList());

            List<FibonacciResultDTO> results = fibonacciResultService.getMostRequestedResultsTop10();

            assertTrue(results.isEmpty());
            verify(fibonacciResultRepository).findTop10ByOrderByQueryCountDesc();
        }
    }
}