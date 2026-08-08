package com.movio.booking.repository;

import com.movio.booking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByBookingId(Long bookingId);
    List<BookingSeat> findByShowSeatId(Long showSeatId);
}
