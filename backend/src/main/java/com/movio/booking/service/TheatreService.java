package com.movio.booking.service;

import com.movio.booking.dto.CreateTheatreRequest;
import com.movio.booking.entity.Theatre;
import com.movio.booking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreService {
    private final TheatreRepository theatreRepository;

    public Theatre createTheatre(CreateTheatreRequest req) {
        Theatre theatre = new Theatre();
        theatre.setName(req.name());
        theatre.setCity(req.city());
        theatre.setAddress(req.address());
        return theatreRepository.save(theatre);
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }
}
