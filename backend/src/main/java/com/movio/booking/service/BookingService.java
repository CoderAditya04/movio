package com.movio.booking.service;

import com.movio.booking.entity.*;
import com.movio.booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ShowSeatRepository showSeatRepository;
    private final PaymentRepository paymentRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final QrCodeService qrCodeService;

    @Transactional
    public Booking createPendingBooking(Long showId, List<Long> showSeatIds, Long userId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double total = 0;
        List<ShowSeat> seats = new java.util.ArrayList<>();

        for (Long seatId : showSeatIds) {
            ShowSeat seat = showSeatRepository.findByIdForUpdate(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));

            // critical validation — this seat must be locked, and locked BY THIS USER
            if (seat.getStatus() != ShowSeat.Status.LOCKED || !userId.equals(seat.getLockedByUserId())) {
                throw new IllegalStateException("Seat " + seatId + " is not locked by you");
            }

            total += seat.getPrice();
            seats.add(seat);
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setStatus(Booking.Status.PENDING);
        booking.setTotalAmount(total);
        booking = bookingRepository.save(booking);

        for (ShowSeat seat : seats) {
            BookingSeat bs = new BookingSeat();
            bs.setBooking(booking);
            bs.setShowSeat(seat);
            bs.setPrice(seat.getPrice());
            bookingSeatRepository.save(bs);
        }

        return booking;
    }

    @Transactional
    public Booking confirmBookingWithPayment(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalStateException("This booking does not belong to you");
        }

        PaymentService.PaymentResult result = paymentService.processPayment(booking.getTotalAmount());

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod("SIMULATED_CARD");
        payment.setTransactionRef(result.transactionRef());

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

        if (result.success()) {
            payment.setStatus(Payment.Status.SUCCESS);
            booking.setStatus(Booking.Status.CONFIRMED);
            booking.setQrPayload(qrCodeService.generateSignedPayload(booking.getId()));

            for (BookingSeat bs : bookingSeats) {
                ShowSeat seat = showSeatRepository.findByIdForUpdate(bs.getShowSeat().getId())
                        .orElseThrow();
                seat.setStatus(ShowSeat.Status.BOOKED);
                showSeatRepository.save(seat);
            }
        } else {
            payment.setStatus(Payment.Status.FAILED);
            booking.setStatus(Booking.Status.CANCELLED);

            for (BookingSeat bs : bookingSeats) {
                ShowSeat seat = showSeatRepository.findByIdForUpdate(bs.getShowSeat().getId())
                        .orElseThrow();
                seat.setStatus(ShowSeat.Status.AVAILABLE);
                seat.setLockedByUserId(null);
                seat.setLockedAt(null);
                showSeatRepository.save(seat);
            }
        }

        paymentRepository.save(payment);
        bookingRepository.save(booking);
        return booking;
    }
}