package com.movio.booking.controller;

import com.movio.booking.dto.BookingDetailResponse;
import com.movio.booking.security.UserPrincipal;
import com.movio.booking.service.BookingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingHistoryController {

    private final BookingHistoryService bookingHistoryService;

    @GetMapping("/history")
    public List<BookingDetailResponse> getHistory(@AuthenticationPrincipal UserPrincipal principal) {
        return bookingHistoryService.getHistory(principal.getUserId());
    }
}
