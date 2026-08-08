package com.movio.booking.service;

import com.movio.booking.dto.CreateScreenRequest;
import com.movio.booking.entity.Screen;
import com.movio.booking.entity.Seat;
import com.movio.booking.entity.Theatre;
import com.movio.booking.repository.ScreenRepository;
import com.movio.booking.repository.SeatRepository;
import com.movio.booking.repository.TheatreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenService {
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final TheatreRepository theatreRepository;

    @Transactional
    public Screen createScreen(CreateScreenRequest req) {
        Theatre theatre = theatreRepository.findById(req.theatreId())
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        Screen screen = new Screen();
        screen.setTheatre(theatre);
        screen.setName(req.name());
        screen.setTotalRows(req.totalRows());
        screen.setTotalColumns(req.totalColumns());
        screen = screenRepository.save(screen);

        generateSeats(screen);
        return screen;
    }

    private void generateSeats(Screen screen) {
        List<Seat> seats = new ArrayList<>();
        for (int r = 0; r < screen.getTotalRows(); r++) {
            char rowLabel = (char) ('A' + r);
            for (int c = 1; c <= screen.getTotalColumns(); c++) {
                Seat seat = new Seat();
                seat.setScreen(screen);
                seat.setRowLabel(String.valueOf(rowLabel));
                seat.setSeatNumber(c);
                // simple rule: last 2 rows are PREMIUM, rest REGULAR — tune later
                seat.setSeatType(r >= screen.getTotalRows() - 2 ? Seat.SeatType.PREMIUM : Seat.SeatType.REGULAR);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }
}
