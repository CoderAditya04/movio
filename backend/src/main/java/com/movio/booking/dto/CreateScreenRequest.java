package com.movio.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateScreenRequest(
        @NotNull(message = "theatreId is required")
        Long theatreId,

        @NotBlank(message = "Screen name is required")
        String name,

        @NotNull(message = "totalRows is required")
        @Min(value = 1, message = "totalRows must be at least 1")
        Integer totalRows,

        @NotNull(message = "totalColumns is required")
        @Min(value = 1, message = "totalColumns must be at least 1")
        Integer totalColumns
) {}