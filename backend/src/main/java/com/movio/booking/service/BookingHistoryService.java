package com.movio.booking.service;

import com.movio.booking.dto.BookingDetailResponse;
import com.movio.booking.entity.Booking;
import com.movio.booking.entity.BookingSeat;
import com.movio.booking.repository.BookingRepository;
import com.movio.booking.repository.BookingSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingHistoryService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public List<BookingDetailResponse> getHistory(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdWithDetails(userId);

        return bookings.stream().map(b -> {
            List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(b.getId());
            List<String> seatLabels = bookingSeats.stream()
                    .map(bs -> bs.getShowSeat().getSeat().getRowLabel() + bs.getShowSeat().getSeat().getSeatNumber())
                    .toList();

            return new BookingDetailResponse(
                    b.getId(),
                    b.getShow().getMovie().getTitle(),
                    b.getShow().getScreen().getTheatre().getName(),
                    b.getShow().getScreen().getName(),
                    b.getShow().getStartTime(),
                    b.getStatus().name(),
                    b.getTotalAmount(),
                    b.getCreatedAt(),
                    seatLabels
            );
        }).toList();
    }
}
