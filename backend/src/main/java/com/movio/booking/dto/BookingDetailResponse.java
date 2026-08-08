package com.movio.booking.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BookingDetailResponse(
        Long bookingId,
        String movieTitle,
        String theatreName,
        String screenName,
        LocalDateTime showTime,
        String status,
        Double totalAmount,
        LocalDateTime bookedAt,
        List<String> seatLabels
) {}
