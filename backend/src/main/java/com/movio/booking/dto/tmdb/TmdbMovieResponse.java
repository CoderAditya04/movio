package com.movio.booking.dto.tmdb;

import java.util.List;

public record TmdbMovieResponse(List<TmdbMovie> results) {}