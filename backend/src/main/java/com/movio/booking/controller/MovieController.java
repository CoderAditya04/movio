package com.movio.booking.controller;

import com.movio.booking.entity.Movie;
import com.movio.booking.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/api/movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/movies/sync")
    public ResponseEntity<String> syncMovies() {
        int count = movieService.syncFromTmdb();
        return ResponseEntity.ok("Synced " + count + " movies from TMDB");
    }
}