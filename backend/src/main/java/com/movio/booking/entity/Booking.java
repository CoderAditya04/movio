package com.movio.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    private Show show;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private Double totalAmount;
    private String qrPayload;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, EXPIRED
    }
}
