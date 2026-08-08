package com.movio.booking.controller;

import com.movio.booking.dto.CreateTheatreRequest;
import com.movio.booking.entity.Theatre;
import com.movio.booking.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TheatreController {
    private final TheatreService theatreService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/theatres")
    public Theatre createTheatre(@Valid @RequestBody CreateTheatreRequest req) {
        return theatreService.createTheatre(req);
    }

    @GetMapping("/api/theatres")
    public List<Theatre> getAllTheatres() {
        return theatreService.getAllTheatres();
    }
}
