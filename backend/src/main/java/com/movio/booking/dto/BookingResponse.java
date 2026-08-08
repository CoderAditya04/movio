package com.movio.booking.dto;

public record BookingResponse(Long bookingId, String status, Double totalAmount, String paymentStatus) {}
