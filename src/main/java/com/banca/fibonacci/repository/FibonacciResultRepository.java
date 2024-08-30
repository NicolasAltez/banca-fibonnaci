package com.banca.fibonacci.repository;

import com.banca.fibonacci.models.entity.FibonacciResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FibonacciResultRepository extends JpaRepository<FibonacciResult, Long> {
    Optional<FibonacciResult> findByPosition(int position);

    List<FibonacciResult> findTop10ByOrderByQueryCountAsc();

    List<FibonacciResult> findTop10ByOrderByQueryCountDesc();
}