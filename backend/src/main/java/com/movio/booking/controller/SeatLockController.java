package com.movio.booking.controller;

import com.movio.booking.dto.LockSeatsRequest;
import com.movio.booking.security.UserPrincipal;
import com.movio.booking.service.SeatLockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SeatLockController {

    private final SeatLockService seatLockService;

    @PostMapping("/api/seats/lock")
    public ResponseEntity<?> lockSeats(@Valid @RequestBody LockSeatsRequest request,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        try {
            seatLockService.lockSeats(request.seatIds(), principal.getUserId());
            return ResponseEntity.ok("Seats locked successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}