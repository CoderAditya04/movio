package com.movio.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private Double amount;
    private String paymentMethod;
    private String transactionRef;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        PENDING, SUCCESS, FAILED
    }
}
