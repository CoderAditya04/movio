package com.movio.booking.service;

import com.movio.booking.entity.ShowSeat;
import com.movio.booking.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final ShowSeatRepository showSeatRepository;

    @Transactional
    public void lockSeats(List<Long> showSeatIds, Long userId) {
        log.info("User {} attempting to lock seats {}", userId, showSeatIds);

        for (Long id : showSeatIds) {
            ShowSeat seat = showSeatRepository.findByIdForUpdate(id)
                    .orElseThrow(() -> {
                        log.warn("Seat {} not found during lock attempt by user {}", id, userId);
                        return new RuntimeException("Seat not found: " + id);
                    });

            if (seat.getStatus() != ShowSeat.Status.AVAILABLE) {
                log.warn("Seat {} unavailable (status={}) — rejecting lock request from user {}",
                        id, seat.getStatus(), userId);
                throw new IllegalStateException("Seat " + id + " is no longer available");
            }

            seat.setStatus(ShowSeat.Status.LOCKED);
            seat.setLockedByUserId(userId);
            seat.setLockedAt(LocalDateTime.now());
            showSeatRepository.save(seat);
        }

        log.info("User {} successfully locked seats {}", userId, showSeatIds);
    }
}