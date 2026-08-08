package com.movio.booking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LockSeatsRequest(
        @NotNull(message = "showId is required")
        Long showId,

        @NotEmpty(message = "seatIds must contain at least one seat")
        List<Long> seatIds
) {}