package com.movio.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    private String rowLabel;   // "A", "B"...
    private Integer seatNumber; // 1, 2, 3...

    @Enumerated(EnumType.STRING)
    private SeatType seatType = SeatType.REGULAR;

    public enum SeatType {
        REGULAR, PREMIUM
    }
}
