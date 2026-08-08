package com.movio.booking.dto;

import java.util.List;

public record LockSeatsResponse(List<Long> lockedSeatIds, List<Long> failedSeatIds, String message) {}