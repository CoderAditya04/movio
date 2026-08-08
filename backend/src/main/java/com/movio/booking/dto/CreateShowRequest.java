package com.movio.booking.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateShowRequest(
        @NotNull(message = "movieId is required")
        Long movieId,

        @NotNull(message = "screenId is required")
        Long screenId,

        @NotNull(message = "startTime is required")
        @Future(message = "startTime must be in the future")
        LocalDateTime startTime,

        @NotNull(message = "endTime is required")
        LocalDateTime endTime,

        @NotNull(message = "basePrice is required")
        @Positive(message = "basePrice must be greater than 0")
        Double basePrice
) {}