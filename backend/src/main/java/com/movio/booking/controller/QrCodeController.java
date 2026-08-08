package com.movio.booking.controller;

import com.movio.booking.entity.Booking;
import com.movio.booking.repository.BookingRepository;
import com.movio.booking.security.UserPrincipal;
import com.movio.booking.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class QrCodeController {

    private final BookingRepository bookingRepository;
    private final QrCodeService qrCodeService;

    @GetMapping("/{bookingId}/qr")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long bookingId,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(principal.getUserId())) {
            return ResponseEntity.status(403).build();
        }
        if (booking.getQrPayload() == null) {
            return ResponseEntity.badRequest().build(); // booking not confirmed yet
        }

        byte[] qrImage = qrCodeService.generateQrImage(booking.getQrPayload());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }

    @PreAuthorize("hasRole('ADMIN')") // simulating "staff scanner" access
    @PostMapping("/verify")
    public ResponseEntity<?> verifyTicket(@RequestBody String payload) {
        boolean valid = qrCodeService.verifyPayload(payload);
        if (!valid) {
            return ResponseEntity.status(400).body("Invalid or tampered ticket");
        }
        Long bookingId = qrCodeService.extractBookingId(payload);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        if (booking.getStatus() != Booking.Status.CONFIRMED) {
            return ResponseEntity.status(400).body("Ticket not valid for entry: " + booking.getStatus());
        }
        return ResponseEntity.ok("Valid ticket for booking #" + bookingId);
    }
}