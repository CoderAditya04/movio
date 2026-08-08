package com.movio.booking.service;

import com.movio.booking.dto.tmdb.TmdbMovie;
import com.movio.booking.entity.Movie;
import com.movio.booking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient;

    public int syncFromTmdb() {
        var response = tmdbClient.fetchNowPlaying();
        int count = 0;

        for (TmdbMovie tm : response.results()) {
            String tmdbId = String.valueOf(tm.id());
            Movie movie = movieRepository.findByTmdbId(tmdbId).orElseGet(Movie::new);

            movie.setTmdbId(tmdbId);
            movie.setTitle(tm.title());
            movie.setDescription(tm.overview());
            movie.setPosterUrl(tm.poster_path() != null
                    ? "https://image.tmdb.org/t/p/w500" + tm.poster_path()
                    : null);
            movie.setLanguage("en"); // TMDB gives original_language too if you want it later

            movieRepository.save(movie);
            count++;
        }
        return count;
    }

    public java.util.List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}