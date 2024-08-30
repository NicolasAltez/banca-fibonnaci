package com.banca.fibonacci.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "fibonacci_results", indexes = {
        @Index(name = "idx_fibonacci_position", columnList = "position", unique = true),
        @Index(name = "idx_fibonacci_query_count", columnList = "query_count")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FibonacciResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position", nullable = false, unique = true)
    private int position;

    @Column(name = "value", nullable = false)
    private long value;

    @Column(name = "query_count", nullable = false)
    private int queryCount;

}
