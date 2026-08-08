package com.movio.booking.service;

import com.movio.booking.dto.tmdb.TmdbMovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TmdbClient {

    private final WebClient webClient;

    @Value("${tmdb.api.key}")
    private String apiKey;

    public TmdbClient(@Value("${tmdb.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public TmdbMovieResponse fetchNowPlaying() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/now_playing")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "en-US")
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .bodyToMono(TmdbMovieResponse.class)
                .block();
    }
}