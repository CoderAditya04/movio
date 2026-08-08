package com.movio.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    private String name; // e.g. "Screen 1"
    private Integer totalRows;
    private Integer totalColumns;
}
