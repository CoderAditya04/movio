package com.movio.booking.repository;

import com.movio.booking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTmdbId(String tmdbId);
}