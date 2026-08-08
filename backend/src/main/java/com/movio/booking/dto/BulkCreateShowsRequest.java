package com.movio.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public record BulkCreateShowsRequest(
        @NotNull Long movieId,
        @NotEmpty List<Long> screenIds,
        @NotEmpty List<@Future LocalDateTime> startTimes,
        @NotNull @Positive Integer durationMinutes,
        @NotNull @Positive Double basePrice
) {}