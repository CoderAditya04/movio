package com.movio.booking.service;

import com.movio.booking.entity.*;
import com.movio.booking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatLockExpiryJob {

    private final ShowSeatRepository showSeatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingRepository bookingRepository;

    private static final int LOCK_DURATION_MINUTES = 5;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredLocks() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(LOCK_DURATION_MINUTES);
        List<ShowSeat> expiredSeats = showSeatRepository.findByStatusAndLockedAtBefore(
                ShowSeat.Status.LOCKED, cutoff);

        if (expiredSeats.isEmpty()) return;

        // find any PENDING bookings tied to these seats, before we clear the seat data
        Set<Long> affectedBookingIds = expiredSeats.stream()
                .flatMap(seat -> bookingSeatRepository.findByShowSeatId(seat.getId()).stream())
                .map(bs -> bs.getBooking().getId())
                .collect(Collectors.toSet());

        for (ShowSeat seat : expiredSeats) {
            seat.setStatus(ShowSeat.Status.AVAILABLE);
            seat.setLockedByUserId(null);
            seat.setLockedAt(null);
        }
        showSeatRepository.saveAll(expiredSeats);

        for (Long bookingId : affectedBookingIds) {
            bookingRepository.findById(bookingId).ifPresent(booking -> {
                if (booking.getStatus() == Booking.Status.PENDING) {
                    booking.setStatus(Booking.Status.EXPIRED);
                    bookingRepository.save(booking);
                }
            });
        }

        if (!expiredSeats.isEmpty()) {
            log.info("Released {} expired seat locks, expired {} stale bookings",
                    expiredSeats.size(), affectedBookingIds.size());
        }
    }
}