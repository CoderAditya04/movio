package com.movio.booking.repository;

import com.movio.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT DISTINCT b FROM Booking b
        JOIN FETCH b.show s
        JOIN FETCH s.movie
        JOIN FETCH s.screen sc
        JOIN FETCH sc.theatre
        LEFT JOIN FETCH b.user
        WHERE b.user.id = :userId
        ORDER BY b.createdAt DESC
        """)
    List<Booking> findByUserIdWithDetails(@Param("userId") Long userId);
}
