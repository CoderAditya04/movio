package com.movio.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "show_seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"show_id", "seat_id"})
})
@Data
public class ShowSeat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    private Long lockedByUserId;
    private LocalDateTime lockedAt;

    private Double price;

    @Version
    private Long version; // optimistic locking safety net — explained below

    public enum Status {
        AVAILABLE, LOCKED, BOOKED
    }
}
