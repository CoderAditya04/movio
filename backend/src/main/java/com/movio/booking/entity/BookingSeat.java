package com.movio.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "booking_seats")
@Data
public class BookingSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_seat_id")
    private ShowSeat showSeat;

    private Double price;
}
