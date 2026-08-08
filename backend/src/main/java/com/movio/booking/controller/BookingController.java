package com.movio.booking.controller;

import com.movio.booking.dto.*;
import com.movio.booking.entity.Booking;
import com.movio.booking.security.UserPrincipal;
import com.movio.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody CreateBookingRequest request,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        try {
            Booking booking = bookingService.createPendingBooking(
                    request.showId(), request.showSeatIds(), principal.getUserId());
            return ResponseEntity.ok(booking);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long bookingId,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            Booking booking = bookingService.confirmBookingWithPayment(bookingId, principal.getUserId());
            return ResponseEntity.ok(booking);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}