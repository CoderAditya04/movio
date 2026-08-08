package com.movio.booking.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private Integer durationMins;

    private String genre;

    private String language;

    private String posterUrl;

    private String tmdbId; // reference back to the third-party source, useful for re-syncing later
}