package com.movio.booking.dto.tmdb;

import java.util.List;

public record TmdbMovie(
        Long id,
        String title,
        String overview,
        String poster_path,
        String release_date,
        List<Integer> genre_ids
) {}